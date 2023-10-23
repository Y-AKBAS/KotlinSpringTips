package com.yakbas.kotlinSpring.common

import com.yakbas.kotlinSpring.db.flyway.FlywayService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class ApplicationReadyEventImpl(private val flywayService: FlywayService) : ApplicationListener<ApplicationReadyEvent> {
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        flywayService.migrate()
    }
}