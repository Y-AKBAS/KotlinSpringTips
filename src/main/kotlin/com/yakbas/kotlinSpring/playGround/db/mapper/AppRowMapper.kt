package com.yakbas.kotlinSpring.playGround.db.mapper

import org.springframework.jdbc.core.RowMapper

abstract class AppRowMapper : RowMapper<Map<String, Any?>>
