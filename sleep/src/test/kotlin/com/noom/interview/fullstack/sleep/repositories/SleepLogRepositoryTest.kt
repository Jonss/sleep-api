@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.noom.interview.fullstack.sleep.repositories

import com.noom.interview.fullstack.sleep.exceptions.EntityNotFoundException
import com.noom.interview.fullstack.sleep.models.entities.SleepLog
import com.noom.interview.fullstack.sleep.models.enums.SleepQuality
import com.noom.interview.fullstack.sleep.repositories.utils.cleanDB
import com.noom.interview.fullstack.sleep.repositories.utils.createUser
import com.noom.interview.fullstack.sleep.utils.endOfDay
import com.noom.interview.fullstack.sleep.utils.startOfDay
import com.noom.interview.fullstack.sleep.utils.yesterday
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.event.annotation.AfterTestClass
import java.sql.Timestamp
import java.time.*
import java.time.temporal.ChronoUnit

@SpringBootTest
@TestPropertySource("classpath:application.repo-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SleepLogRepositoryTest {
    @Autowired
    private lateinit var sleepLogRepository: SleepLogRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private val externalUserId = "63e8eafc-6156-4f5f-83df-8116cce4ec71"

    @BeforeEach
    fun setUp() {
        cleanDB(jdbcTemplate)
    }

    @AfterTestClass
    fun tearDown() {
        cleanDB(jdbcTemplate)
    }

    @Test
    fun shouldCreateSleepLog() {
        // given
        val userId = createUser(userRepository)
        val sleepLog = SleepLog(0, userId, Instant.now().minus(8, ChronoUnit.HOURS), Instant.now(), SleepQuality.OK, Instant.now())

        // when
        sleepLogRepository.create(sleepLog)

        val sleepLogId =
            jdbcTemplate.queryForObject(
                "select id from sleep where user_id = ? order by id desc limit 1",
                arrayOf(userId),
                Int::class.java,
            )

        // then
        assertNotEquals(0, sleepLogId)
    }

    @Test
    fun shouldGetExceptionWhenUsersDoesNotExistsCreateSleepLog() {
        val nonExistingUserId = 1000L

        val sleepLog =
            SleepLog(0, nonExistingUserId, Instant.now().minus(8, ChronoUnit.HOURS), Instant.now(), SleepQuality.OK, Instant.now())

        assertThrows(EntityNotFoundException::class.java, { sleepLogRepository.create(sleepLog) })
    }

    @Test
    fun shouldFindASleepLogGivenAnInterval() {
        // given
        val userId = createUser(userRepository)

        val from = startOfDay(yesterday)
        val to = endOfDay(yesterday)
        val createdAt = Instant.now().minus(23, ChronoUnit.HOURS)
        val sleepLog =
            SleepLog(
                id = 0,
                userId = userId,
                startDate = yesterday,
                endDate = yesterday.plus(7, ChronoUnit.HOURS),
                quality = SleepQuality.OK,
                createdAt = createdAt,
            )

        // when
        sleepLogRepository.create(sleepLog)
        // by pass on created_at behaviour on db
        jdbcTemplate.update("update sleep set created_at = ? ", Timestamp.from(createdAt))

        // then
        val yesterdaySleepLogs = sleepLogRepository.fetchByUserIdFromInterval(userId, from, to)
        val yesterdaySleepLog = yesterdaySleepLogs[0]
        assertEquals(1, yesterdaySleepLogs.size)
        assertNotNull(yesterdaySleepLog)
        assertNotEquals(0, yesterdaySleepLog.id)
        assertEquals(userId, yesterdaySleepLog.user.id)
        assertEquals(externalUserId, yesterdaySleepLog.user.externalId)
        assertEquals(SleepQuality.OK, yesterdaySleepLog.quality)
        assertEquals(sleepLog.startDate, yesterdaySleepLog.startDate)
        assertEquals(sleepLog.endDate, yesterdaySleepLog.endDate)
    }

    @Test
    fun shouldGetNullWhenUserDoesNotExist() {
        val nonExistingUserId = 1000L

        val from = startOfDay(yesterday)
        val to = endOfDay(yesterday)

        val yesterdaySleepLogs = sleepLogRepository.fetchByUserIdFromInterval(nonExistingUserId, from, to)
        assertTrue(yesterdaySleepLogs.isEmpty())
    }
}
