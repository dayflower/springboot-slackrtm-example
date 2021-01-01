package com.example.springboot.slackrtm

import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component
class SlackRtmLifeCycle(
    private val slackRtmManager: SlackRtmManager
) {

    @PostConstruct
    fun postConstruct() {
        slackRtmManager.start()
    }

    @PreDestroy
    fun preDestroy() {
        slackRtmManager.stop()
    }

}
