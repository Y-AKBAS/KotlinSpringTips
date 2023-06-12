package com.yakbas.kotlinSpring

import com.yakbas.kotlinSpring.db.EmployeeType
import com.yakbas.kotlinSpring.db.TestUserEntity
import com.yakbas.kotlinSpring.db.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class KotlinSpringApplication {

    @Bean
    fun commandLineRunner(userRepository: UserRepository): CommandLineRunner {
        return CommandLineRunner {
            userRepository.save(
                TestUserEntity(
                    name = "Yasin",
                    age = 28,
                    employeeType = EmployeeType.SOFTWARE_ENGINEER
                )
            )
        }
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinSpringApplication>(*args)
}


