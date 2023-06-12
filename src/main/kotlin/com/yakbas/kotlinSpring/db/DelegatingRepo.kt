package com.yakbas.kotlinSpring.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@NoRepositoryBean
open class DelegatingRepo<ENTITY, ID, REPOSITORY : CrudRepository<ENTITY, ID>>(
    protected val repository: REPOSITORY
) : CrudRepository<ENTITY, ID> by repository {

    @Autowired
    protected lateinit var jdbcTemplate: NamedParameterJdbcTemplate
}
