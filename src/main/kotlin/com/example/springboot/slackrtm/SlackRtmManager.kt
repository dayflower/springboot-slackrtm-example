package com.example.springboot.slackrtm

import com.slack.api.model.event.HelloEvent
import com.slack.api.rtm.RTMClient
import com.slack.api.rtm.RTMEventHandler
import com.slack.api.rtm.RTMEventsDispatcherFactory
import com.slack.api.rtm.message.PingMessage
import mu.KotlinLogging
import java.time.Instant
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class SlackRtmManager(
    private val rtmClient: RTMClient,
    private val scheduledExecutorService: ScheduledExecutorService,
) {
    companion object {
        private val log = KotlinLogging.logger { }
    }

    @Volatile
    private var wantShutdown: Boolean = false

    @Volatile
    private var connected: Boolean = false

    private var pingTaskScheduledFuture: ScheduledFuture<*>? = null

    init {
        val dispatcher = RTMEventsDispatcherFactory.getInstance().apply {
            register(object : RTMEventHandler<HelloEvent>() {
                override fun handle(event: HelloEvent) {
                    log.info { "hello" }

                    connected = true

                    startPinging()
                }
            })
        }

        rtmClient.addMessageHandler(dispatcher.toMessageHandler())
    }

    fun start() {
        wantShutdown = false
        connected = false

        rtmClient.connect()
    }

    fun stop() {
        log.info { "Terminating Slack RTM ..." }

        wantShutdown = true
        connected = false

        pingTaskScheduledFuture?.cancel(true)
        pingTaskScheduledFuture = null

        rtmClient.disconnect()
    }

    private fun startPinging() {
        pingTaskScheduledFuture?.cancel(true)

        pingTaskScheduledFuture =
            scheduledExecutorService.scheduleAtFixedRate(PingTask(), 1L, 60L, TimeUnit.SECONDS)
    }

    inner class PingTask : Runnable {
        private var id: Long = 0

        override fun run() {
            if (wantShutdown || !connected) {
                return
            }

            log.info { "Pinging to Slack ..." }

            try {
                id += 1
                val message = PingMessage.builder().id(id).time(Instant.now()).build()
                rtmClient.sendMessage(message.toJSONString())
            } catch (e: Exception) {
                log.error(e) { "Connection maybe lost. Try to reconnect." }

                rtmClient.reconnect()
            }
        }
    }
}