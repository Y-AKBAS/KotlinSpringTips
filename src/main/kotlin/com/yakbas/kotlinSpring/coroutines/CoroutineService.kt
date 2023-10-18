package com.yakbas.kotlinSpring.coroutines

import com.yakbas.kotlinSpring.common.Client
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import kotlin.system.measureTimeMillis

@Service
class CoroutineService(private val client: Client, private val dispatchers: AppCoroutineDispatcher) {

    fun getSync(): PublicApiResult {
        var result = PublicApiResult(count = 0, entries = emptyList())

        val millis = measureTimeMillis {
            repeat(3) { result += client.get() }
        }
        println("Passed time in sync: $millis")
        return result
    }

    fun getAsync(): PublicApiResult = runBlocking(dispatchers.dispatcher) {
        var result = PublicApiResult(count = 0, entries = emptyList())
        val millis = measureTimeMillis {
            val deferredList = List(3) { async { client.get() } }
            deferredList.awaitAll().forEach { result += it }
        }
        println("passed time in async: $millis")
        return@runBlocking result
    }

}
