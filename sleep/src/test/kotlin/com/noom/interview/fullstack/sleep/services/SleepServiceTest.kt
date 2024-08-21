package com.noom.interview.fullstack.sleep.services

import com.nhaarman.mockitokotlin2.any
import com.noom.interview.fullstack.sleep.repositories.SleepLogRepository
import com.noom.interview.fullstack.sleep.services.stubs.singleSleepLogsStub
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SleepServiceTest {
    @Mock
    lateinit var mockRepository: SleepLogRepository

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
}
