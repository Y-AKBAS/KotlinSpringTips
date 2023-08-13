package com.yakbas.kotlinSpring.coroutines

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/coroutines")
class PublicApisResource(private val coroutineService: CoroutineService) {

    @GetMapping("/sync")
    fun sync(): ResponseEntity<PublicApiResult> {
        val result = coroutineService.get()
        return ResponseEntity.ok().body(result)
    }

    @GetMapping("/async")
    fun async(): ResponseEntity<PublicApiResult> {
        val result = coroutineService.getAsync()
        return ResponseEntity.ok().body(result)
    }
}