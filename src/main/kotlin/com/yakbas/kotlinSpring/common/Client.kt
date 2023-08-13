package com.yakbas.kotlinSpring.common

import com.yakbas.kotlinSpring.coroutines.PublicApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class Client(private val template: RestTemplate) {

    fun get(): PublicApiResult {
        Thread.sleep(1000)
        val result =
            template.getForObject("https://api.publicapis.org/entries", PublicApiResult::class.java)
        return result!!
    }
}
