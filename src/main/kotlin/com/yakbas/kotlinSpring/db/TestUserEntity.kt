package com.yakbas.kotlinSpring.db

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("test_user")
class TestUserEntity(
    @Id var id: Long?,
    var name: String?,
    var age: Int?
)
