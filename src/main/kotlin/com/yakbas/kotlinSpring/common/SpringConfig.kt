package com.yakbas.kotlinSpring.common

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.security.SecureRandom


@Configuration
class SpringConfig {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }

    @Bean
    fun secureRandom(): SecureRandom = SecureRandom.getInstanceStrong()
}
