package com.yakbas.kotlinSpring.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.context.Lifecycle
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy
import kotlin.coroutines.CoroutineContext

@Component
class AppCoroutineScope: CoroutineScope, Lifecycle {

    private val supervisorJob = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.IO + supervisorJob

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        supervisorJob.cancel()
    }

    override fun isRunning(): Boolean {
        return !supervisorJob.isCancelled
    }

    @PreDestroy
    fun cancelJobs(){
        supervisorJob.cancel()
    }
}
