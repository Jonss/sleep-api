package com.noom.interview.fullstack.sleep.repositories.utils

import com.noom.interview.fullstack.sleep.models.entities.User
import com.noom.interview.fullstack.sleep.repositories.UserRepository
import org.springframework.jdbc.core.JdbcTemplate

fun cleanDB(jdbcTemplate: JdbcTemplate) {
    jdbcTemplate.update("TRUNCATE TABLE sleep, users CASCADE")
}

fun createUser(
    userRepository: UserRepository,
    externalUserId: String = "63e8eafc-6156-4f5f-83df-8116cce4ec71",
): Long {
    userRepository.create(User(0, externalUserId))
    return userRepository.findUserByExternalId(externalUserId)?.id ?: 0L
}
