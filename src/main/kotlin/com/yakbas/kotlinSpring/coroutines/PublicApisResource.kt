package com.yakbas.kotlinSpring.coroutines

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping



@RestController
@RequestMapping("/coroutines")
class PublicApisResource(private val coroutineService: CoroutineService, private val ctx: ApplicationContext) {
    
    @PostConstruct
    fun dumpHandlerMappings() {
        println("=== Handler Mappings ===")
        ctx.getBeansOfType(RequestMappingInfoHandlerMapping::class.java)
            .values
            .forEach { mapping ->
                mapping.handlerMethods.forEach { (info, method) ->
                    println("${info.methodsCondition} ${info.patternsCondition} -> ${method.beanType.simpleName}.${method.method.name}")
                }
            }
        println("=== End Handler Mappings ===")
    }

    @GetMapping("/sync")
    fun sync(): ResponseEntity<PublicApiResult> {
        val result = coroutineService.getSync()
        return ResponseEntity.ok().body(result)
    }

    @GetMapping("/async")
    fun async(): ResponseEntity<PublicApiResult> {
        val result = coroutineService.getAsync()
        return ResponseEntity.ok().body(result)
    }
}
