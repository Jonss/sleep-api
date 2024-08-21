package com.noom.interview.fullstack.sleep.repositories.utils

import org.springframework.jdbc.core.JdbcTemplate

fun cleanDB(jdbcTemplate: JdbcTemplate) {
    jdbcTemplate.update("TRUNCATE TABLE sleep, users CASCADE")
}
