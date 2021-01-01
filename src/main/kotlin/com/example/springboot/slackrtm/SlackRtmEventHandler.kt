package com.example.springboot.slackrtm

import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.model.event.HelloEvent
import com.slack.api.model.event.MessageEvent
import com.slack.api.model.event.PongEvent
import com.slack.api.model.event.ReactionAddedEvent
import com.slack.api.rtm.RTMClient
import com.slack.api.rtm.RTMEventHandler
import com.slack.api.rtm.RTMEventsDispatcherFactory
import mu.KotlinLogging

class SlackRtmEventHandler(
    @Suppress("CanBeParameter")
    private val rtmClient: RTMClient,
    private val methodsClient: MethodsClient,
) {

    companion object {
        @Suppress("unused")
        private val log = KotlinLogging.logger { }
    }

    init {
        val dispatcher = RTMEventsDispatcherFactory.getInstance().apply {
            register(object : RTMEventHandler<HelloEvent>() {
                override fun handle(event: HelloEvent) {
                    onHello(event)
                }
            })

            register(object : RTMEventHandler<PongEvent>() {
                override fun handle(event: PongEvent) {
                    onPong(event)
                }
            })

            register(object : RTMEventHandler<MessageEvent>() {
                override fun handle(event: MessageEvent) {
                    onMessage(event)
                }
            })

            register(object : RTMEventHandler<ReactionAddedEvent>() {
                override fun handle(event: ReactionAddedEvent) {
                    onReactionAdded(event)
                }
            })
        }

        rtmClient.addMessageHandler(dispatcher.toMessageHandler())
    }

    private fun onHello(event: HelloEvent) {
        log.info { "hello: $event" }
    }

    private fun onPong(event: PongEvent) {
        log.info { "pong: $event" }
    }

    private fun onMessage(event: MessageEvent) {
        log.info { "message: $event" }
    }

    private fun onReactionAdded(event: ReactionAddedEvent) {
        log.info { "reaction_added: $event" }

        methodsClient.chatPostMessage(
            ChatPostMessageRequest.builder()
                .channel(event.item.channel)
                .text(":${event.reaction}:")
                .threadTs(event.item.ts)
                .build()
        )
    }

}
