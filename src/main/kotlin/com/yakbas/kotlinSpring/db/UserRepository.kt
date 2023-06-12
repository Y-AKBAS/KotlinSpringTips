package com.yakbas.kotlinSpring.db

import org.springframework.stereotype.Repository


@Repository
class UserRepository(
    rawRepository: SimpleUserRepository
) : DelegatingRepo<TestUserEntity, Long, SimpleUserRepository>(rawRepository)
