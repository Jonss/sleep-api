package com.noom.interview.fullstack.sleep.repositories

import com.noom.interview.fullstack.sleep.models.entities.SleepLog
import com.noom.interview.fullstack.sleep.models.entities.User
import com.noom.interview.fullstack.sleep.models.enums.SleepQuality
import com.noom.interview.fullstack.sleep.repositories.utils.cleanDB
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.event.annotation.AfterTestClass
import java.time.Instant
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
        userRepository.create(User(0, externalUserId))
        val userId = userRepository.findUserByExternalId(externalUserId)?.id ?: 0L

        val sleepLog = SleepLog(0, userId, Instant.now().minus(8, ChronoUnit.HOURS), Instant.now(), SleepQuality.OK, Instant.now())

        val sleepLogId = sleepLogRepository.create(sleepLog)
        assertNotEquals(0, sleepLogId)
    }
}
