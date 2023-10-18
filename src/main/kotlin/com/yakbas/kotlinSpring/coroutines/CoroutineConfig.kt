package com.yakbas.kotlinSpring.coroutines

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class CoroutineConfig {

    @Bean
    fun appDispatchers(): AppCoroutineDispatcher {
        return AppCoroutineDispatcher(Executors.newFixedThreadPool(15).asCoroutineDispatcher())
    }
}

class AppCoroutineDispatcher(val dispatcher: ExecutorCoroutineDispatcher)
