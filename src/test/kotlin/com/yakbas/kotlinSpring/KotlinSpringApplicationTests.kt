package com.yakbas.kotlinSpring

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties = ["spring.profiles.active=dev"])
class KotlinSpringApplicationTests {

	@Test
	fun contextLoads() {
	}

}

