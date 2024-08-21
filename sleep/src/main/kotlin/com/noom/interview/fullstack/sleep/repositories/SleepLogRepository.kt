package com.noom.interview.fullstack.sleep.repositories

import com.noom.interview.fullstack.sleep.exceptions.EntityNotFoundException
import com.noom.interview.fullstack.sleep.models.dtos.SleepLogDTO
import com.noom.interview.fullstack.sleep.models.entities.SleepLog
import com.noom.interview.fullstack.sleep.models.utils.SleepRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.Instant

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
            if (e.mostSpecificCause.message?.contains("fk_user_id") == true) {
                throw EntityNotFoundException("user")
            }
            throw e
        }
    }

    fun fetchByUserIdFromInterval(
        userId: Long,
        from: Instant,
        to: Instant,
    ): List<SleepLogDTO> {
        val sql = """
            SELECT s.id, s.quality, s.start_date, s.end_date, s.created_at, u.external_id, u.id as user_id
            FROM sleep s
            INNER JOIN users u ON s.user_id = u.id
            WHERE s.user_id = ?
            AND s.created_at BETWEEN ? AND ?
            """

        return try {
            jdbcTemplate.query(
                sql,
                SleepRowMapper(),
                userId,
                Timestamp.from(from),
                Timestamp.from(to),
            )
        } catch (e: EmptyResultDataAccessException) {
            emptyList() // Return an empty list instead of null
        }
    }
}
