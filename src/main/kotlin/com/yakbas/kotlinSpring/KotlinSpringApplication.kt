package com.yakbas.kotlinSpring

import com.yakbas.kotlinSpring.common.Client
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class KotlinSpringApplication {

    @Bean
    fun commandLineRunner(client: Client): CommandLineRunner {
        return CommandLineRunner {

        }
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinSpringApplication>(*args)
}


