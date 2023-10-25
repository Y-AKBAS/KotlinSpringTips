package com.yakbas.kotlinSpring.db.flyway

import org.flywaydb.core.Flyway
import org.springframework.stereotype.Service
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.CleanResult
import org.flywaydb.core.api.output.MigrateResult
import org.springframework.beans.factory.annotation.Autowired

@Service
class FlywayService(@Autowired(required = false) private val flyway: Flyway?) {

    private val logger = createLogger()

    init {
        if (flyway == null) logger.warn("Flyway is deactivated. Migrations won't take place...")
    }

    fun checkStatusAndMigrate(appProperties: AppProperties) {
        if (appProperties.resetDbSchemas) {
            check(flyway != null) { "Flyway should be enabled to be able to clear db schemas" }
            logger.warn("Flyway will clean DB schemas.")
            cleanSafely()
        }
        migrateSafely()
    }

    fun getInfoResult() = flyway?.info()?.infoResult

    fun cleanSafely(): CleanResult? {
        if (flyway == null) return null

        return flyway.clean()
            .takeIf { it != null && !it.schemasCleaned.isNullOrEmpty() } ?: run {
            logger.error("Flyway failed to clean db")
            throw IllegalStateException("DB is not cleaned by Flyway")
        }
    }

    fun migrateSafely(): MigrateResult? {
        if (flyway == null) return null

        return flyway.migrate()
            .takeIf { it != null && it.success } ?: run {
            logger.error("Flyway failed to migrate db")
            throw IllegalStateException("Flyway DB migration is failed")
        }
    }
}
