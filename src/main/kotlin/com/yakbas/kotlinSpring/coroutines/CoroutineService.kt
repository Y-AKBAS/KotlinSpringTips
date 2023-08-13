package com.yakbas.kotlinSpring.coroutines

import com.yakbas.kotlinSpring.common.Client
import com.yakbas.kotlinSpring.common.measureTime
import kotlinx.coroutines.*
import org.springframework.stereotype.Service

@Service
class CoroutineService(private val client: Client) {

    fun get(): PublicApiResult {
        var result = PublicApiResult(count = 0, entries = emptyList())

        measureTime("Passed time in normal:") {
            repeat(3) { result += client.get() }
        }
        return result
    }

    fun getAsync(): PublicApiResult {
        var result = PublicApiResult(count = 0, entries = emptyList())

        var deferredList: List<Deferred<PublicApiResult>> = emptyList()
        measureTime("Passed time in async:") {
            val job = CoroutineScope(Dispatchers.IO).launch {
                deferredList = List(3) { async { client.get() } }
                deferredList.awaitAll().forEach { result += it }
            }
            runBlocking{job.join()}
        }

        return result
    }
}
