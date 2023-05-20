package com.yakbas.kotlinSpring.playGround.db

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
open class DelegatingRepo<ENTITY, ID, REPOSITORY: CrudRepository<ENTITY, ID>>(
    protected val repository: REPOSITORY
): CrudRepository<ENTITY, ID> by repository
