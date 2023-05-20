package com.yakbas.kotlinSpring.playGround.platform

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("app")
class ApplicationProperties {

    lateinit var platform: String

}