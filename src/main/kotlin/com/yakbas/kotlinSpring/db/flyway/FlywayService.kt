package com.yakbas.kotlinSpring.db.flyway

import org.flywaydb.core.Flyway
import org.springframework.stereotype.Service

@Service
class FlywayService(private val flyway: Flyway) {

    fun migrate(){
        flyway.migrate()
    }
}