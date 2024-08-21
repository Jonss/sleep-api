package com.noom.interview.fullstack.sleep.models.utils

import com.noom.interview.fullstack.sleep.models.entities.User
import org.springframework.jdbc.core.RowMapper

class UserRowMapper : RowMapper<User> {
    override fun mapRow(
        rs: java.sql.ResultSet,
        rowNum: Int,
    ): User =
        User(
            id = rs.getLong("id"),
            externalId = rs.getString("external_id"),
        )
}
