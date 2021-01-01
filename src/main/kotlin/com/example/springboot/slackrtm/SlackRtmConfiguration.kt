package com.example.springboot.slackrtm

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.rtm.RTMClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

@Configuration
@EnableConfigurationProperties(SlackProperties::class)
class SlackRtmConfiguration(
    private val slackProperties: SlackProperties
) {

    @Bean
    fun slackRtmManager(
        rtmClient: RTMClient,
        scheduledExecutorService: ScheduledExecutorService,
        // ensure SlackRtmEventHandler already exists and initialized
        @Suppress("unused")
        slackRtmEventHandler: SlackRtmEventHandler
    ): SlackRtmManager {
        return SlackRtmManager(rtmClient, scheduledExecutorService)
    }

    @Bean
    fun slackRtmEventHandler(
        rtmClient: RTMClient,
        methodsClient: MethodsClient
    ): SlackRtmEventHandler {
        return SlackRtmEventHandler(rtmClient, methodsClient)
    }

    @Bean
    fun methodsClient(): MethodsClient {
        return Slack.getInstance().methods(slackProperties.token)
    }

    @Bean
    fun rtmClient(): RTMClient {
        return Slack.getInstance().rtmConnect(slackProperties.token)
    }

    @Bean
    fun scheduledExecutorService(): ScheduledExecutorService {
        return Executors.newSingleThreadScheduledExecutor()
    }

}
