package com.noom.interview.fullstack.sleep.repositories

import com.noom.interview.fullstack.sleep.models.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun create(user: User) = jdbcTemplate.update("INSERT INTO users (external_id) VALUES (?)", user.externalId)

    fun findUserByExternalId(externalId: String) =
        jdbcTemplate.queryForObject(
            "SELECT id, external_id FROM users WHERE external_id = ?",
            arrayOf(1212L),
        ) { rs, _ ->
            User(rs.getLong("id"), rs.getString("external_id"))
        }
}
