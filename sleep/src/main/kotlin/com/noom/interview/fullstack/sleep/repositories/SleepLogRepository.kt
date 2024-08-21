package com.noom.interview.fullstack.sleep.repositories

import com.noom.interview.fullstack.sleep.exceptions.EntityNotFoundException
import com.noom.interview.fullstack.sleep.models.entities.SleepLog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
class SleepLogRepository {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun create(sleepLog: SleepLog) {
        try {
            jdbcTemplate.update(
                "INSERT INTO sleep (start_date, end_date, quality, user_id) VALUES (?, ?, ?::sleep_quality, ?)",
                Timestamp.from(sleepLog.startDate),
                Timestamp.from(sleepLog.endDate),
                sleepLog.quality.name,
                sleepLog.userId,
            )
        } catch (e: DataIntegrityViolationException) {
            if (e.mostSpecificCause?.message?.contains("fk_user_id") == true) {
                throw EntityNotFoundException("user")
            }
            throw e
        }
    }
}
