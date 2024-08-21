package com.noom.interview.fullstack.sleep.repositories

import com.noom.interview.fullstack.sleep.models.entities.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.event.annotation.AfterTestClass

@SpringBootTest
@TestPropertySource("classpath:application.repo-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun truncateUsers() {
        jdbcTemplate.update("TRUNCATE TABLE users")
    }

    @BeforeEach
    fun setUp() {
        truncateUsers()
    }

    @AfterTestClass
    fun tearDown() {
        truncateUsers()
    }

    @Test
    fun shouldCreateUser() {
        // given
        val user = User(0, "86b4362e-a2cb-49d7-ba40-9f066bfeb707")

        // when
        userRepository.create(user)

        // then
        val userFound = userRepository.findUserByExternalId(user.externalId)
        assertNotNull(userFound)
        assertNotEquals(0, userFound?.id)
        assertEquals(user.externalId, userFound?.externalId)
    }

    @Test
    fun shouldNotFindUser() {
        // given && when
        val userFound = userRepository.findUserByExternalId("non-existing-external-id")

        // then
        assertNull(userFound)
    }
}
