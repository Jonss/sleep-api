package com.noom.interview.fullstack.sleep.services

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.noom.interview.fullstack.sleep.exceptions.EntityNotFoundException
import com.noom.interview.fullstack.sleep.models.dtos.IntervalDTO
import com.noom.interview.fullstack.sleep.models.dtos.SleepLogRequestDTO
import com.noom.interview.fullstack.sleep.models.enums.SleepQuality
import com.noom.interview.fullstack.sleep.repositories.SleepLogRepository
import com.noom.interview.fullstack.sleep.repositories.UserRepository
import com.noom.interview.fullstack.sleep.services.stubs.multipleSleepLogs
import com.noom.interview.fullstack.sleep.services.stubs.singleSleepLogsStub
import com.noom.interview.fullstack.sleep.services.stubs.user
import com.noom.interview.fullstack.sleep.utils.endOfDay
import com.noom.interview.fullstack.sleep.utils.startOfDay
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.time.temporal.ChronoUnit

@ExtendWith(MockitoExtension::class)
class SleepServiceTest {
    @Mock
    lateinit var mockRepository: SleepLogRepository

    @Mock
    lateinit var userMockRepository: UserRepository

    @InjectMocks
    private lateinit var sleepService: SleepService

    @Test
    fun shouldGetLastNightSleepLog() {
        // given
        `when`(mockRepository.fetchByUserIdFromInterval(any(), any(), any())).thenReturn(singleSleepLogsStub)

        // when
        val sleepLog = sleepService.getLastNightSleepLog(1)

        // then
        assertNotNull(sleepLog)
        verify(mockRepository, times(1)).fetchByUserIdFromInterval(any(), any(), any())
    }

    @Test
    fun shouldNotGetLastNightSleepLogWhenListIsEmpty() {
        // given
        `when`(mockRepository.fetchByUserIdFromInterval(any(), any(), any())).thenReturn(emptyList())

        // when
        val sleepLog = sleepService.getLastNightSleepLog(1)

        // then
        assertNull(sleepLog)
        verify(mockRepository, times(1)).fetchByUserIdFromInterval(any(), any(), any())
    }

    @Test
    fun shouldGetSleepLogDataFromLastNDays() {
        // given
        val thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS)
        val from = startOfDay(thirtyDaysAgo)
        val to = endOfDay(Instant.now())
        val expectedSleepQualities = mapOf(SleepQuality.GOOD to 9, SleepQuality.OK to 10, SleepQuality.BAD to 11)

        `when`(mockRepository.fetchByUserIdFromInterval(any(), any(), any())).thenReturn(multipleSleepLogs)

        // when
        val sleepData = sleepService.getSleepLogDataFromLastNDays(1)

        // then
        assertNotNull(sleepData)
        verify(mockRepository, times(1)).fetchByUserIdFromInterval(any(), any(), any())
        assertEquals(IntervalDTO(startDate = from, endDate = to), sleepData?.interval)
        assertEquals("8 h 0 min", sleepData?.avgTotalTimeInBed)
        assertEquals("6 h 32 min", sleepData?.avgTimeGetsToBed)
        assertEquals("14 h 32 min", sleepData?.avgTimeGetsOutOfBed)
        assertEquals(expectedSleepQualities, sleepData?.sleepQualities)
    }

    @Test
    fun shouldGetEmptySleepLogDataFromLastNDays() {
        // given
        val thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS)
        val from = startOfDay(thirtyDaysAgo)
        val to = endOfDay(Instant.now())

        `when`(mockRepository.fetchByUserIdFromInterval(any(), any(), any())).thenReturn(emptyList())

        // when
        val sleepData = sleepService.getSleepLogDataFromLastNDays(1)

        // then
        assertNotNull(sleepData)
        verify(mockRepository, times(1)).fetchByUserIdFromInterval(any(), any(), any())
        assertEquals(IntervalDTO(startDate = from, endDate = to), sleepData?.interval)
        assertEquals("", sleepData?.avgTotalTimeInBed)
        assertEquals("", sleepData?.avgTimeGetsToBed)
        assertEquals("", sleepData?.avgTimeGetsOutOfBed)
        assertEquals(emptyMap<SleepQuality, Int>(), sleepData?.sleepQualities)
    }

    @Test
    fun shouldCreateSleepLog() {
        // given
        doNothing()
            .`when`(mockRepository)
            .create(any())
        `when`(userMockRepository.findUserByExternalId(any())).thenReturn(user)

        // when
        sleepService.createSleepLog(
            "external-id",
            SleepLogRequestDTO(
                startDate = Instant.now(),
                endDate = Instant.now(),
                quality = SleepQuality.GOOD,
            ),
        )

        // then
        verify(mockRepository, times(1)).create(any())
    }

    @Test
    fun shouldThrowWhenCreateSleepLog() {
        val sleepLog =
            SleepLogRequestDTO(
                startDate = Instant.now(),
                endDate = Instant.now(),
                quality = SleepQuality.GOOD,
            )

        assertThrows(EntityNotFoundException::class.java, {
            sleepService.createSleepLog(
                "external-id",
                sleepLog,
            )
        })
    }
}
