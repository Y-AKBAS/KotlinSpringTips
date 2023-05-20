package com.yakbas.kotlinSpring.db

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class UserRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    rawRepository: SimpleUserRepository
) : DelegatingRepo<TestUserEntity, Long, SimpleUserRepository>(rawRepository)
