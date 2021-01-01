package com.example.springboot.slackrtm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SlackRtmApplication

fun main(args: Array<String>) {
	runApplication<SlackRtmApplication>(*args)
}
