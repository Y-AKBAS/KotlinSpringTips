package com.yakbas.kotlinSpring.playGround.db.mapper

import java.sql.ResultSet

class UserMapper : AppRowMapper() {
    override fun mapRow(rs: ResultSet, rowNum: Int): Map<String, Any?> {
        val nameStr = "name"
        val ageStr = "age"
        return mapOf(nameStr to rs.getString(nameStr), ageStr to rs.getInt(ageStr))
    }
}
