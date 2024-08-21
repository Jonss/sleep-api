package com.noom.interview.fullstack.sleep.models.utils

import com.noom.interview.fullstack.sleep.models.dtos.SleepLogDTO
import com.noom.interview.fullstack.sleep.models.entities.User
import com.noom.interview.fullstack.sleep.models.enums.SleepQuality
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

class SleepRowMapper : RowMapper<SleepLogDTO> {
    override fun mapRow(
        rs: java.sql.ResultSet,
        rowNum: Int,
    ): SleepLogDTO =
        SleepLogDTO(
            id = rs.getLong("id"),
            user =
                User(
                    id = rs.getLong("user_id"), // Assuming 'id' here is 'users.id' as per query
                    externalId = rs.getString("external_id"), // Same as above
                ),
            quality = SleepQuality.valueOf(rs.getString("quality")),
            startDate = rs.getTimestamp("start_date").toInstant(),
            endDate = rs.getTimestamp("end_date").toInstant(),
            createdAt = rs.getTimestamp("created_at").toInstant(),
        )
}
