package com.noom.interview.fullstack.sleep.repositories

import com.noom.interview.fullstack.sleep.models.entities.User
import com.noom.interview.fullstack.sleep.models.utils.UserRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun create(user: User) = jdbcTemplate.update("INSERT INTO users (external_id) VALUES (?)", user.externalId)

    fun findUserByExternalId(externalId: String): User? {
        val sql = "SELECT id, external_id FROM users WHERE external_id = ?"
        return jdbcTemplate.queryForObject(sql, UserRowMapper(), externalId)
    }

    fun deleteAll() = jdbcTemplate.update("DELETE FROM users")
}
