package com.yakbas.kotlinSpring.db.mapper

import org.springframework.jdbc.core.RowMapper

abstract class AppRowMapper : RowMapper<Map<String, Any?>>
