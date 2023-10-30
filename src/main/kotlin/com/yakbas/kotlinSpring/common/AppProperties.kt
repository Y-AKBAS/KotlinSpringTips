package com.yakbas.kotlinSpring.common

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
class AppProperties(val resetDbSchemas: Boolean)
