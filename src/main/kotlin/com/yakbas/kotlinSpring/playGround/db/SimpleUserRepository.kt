package com.yakbas.kotlinSpring.playGround.db

import com.yakbas.kotlinSpring.playGround.db.mapper.UserMapper
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository


//@Repository
interface SimpleUserRepository : CrudRepository<TestUserEntity, Long> {
    @Query("select name from test_user where id= :id")
    fun findNameById(id: Long): String?

    @Query("select name, age from test_user where id= :id", rowMapperClass = UserMapper::class)
    fun findNameAndAgeById(id: Long): Map<String, Any?>?

    @Query("select name, age from test_user where id= :id")
    fun findNameAndAgeByIdWithEntity(id: Long): TestUserEntity?
}
