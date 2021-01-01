package com.example.springboot.slackrtm

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@ConfigurationProperties(prefix = "slack")
@Validated
@ConstructorBinding
data class SlackProperties(
    @field:NotEmpty
    val token: String
)
