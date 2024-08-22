package com.noom.interview.fullstack.sleep.models.dtos

import com.noom.interview.fullstack.sleep.models.entities.User
import com.noom.interview.fullstack.sleep.models.enums.SleepQuality
import java.time.Instant

data class SleepLogDTO(
    val id: Long,
    val user: User,
    val startDate: Instant,
    val endDate: Instant,
    val quality: SleepQuality,
    val createdAt: Instant,
)

data class SleepLogRequestDTO(
    val startDate: Instant,
    val endDate: Instant,
    val quality: SleepQuality,
)

data class IntervalDTO(
    val startDate: Instant,
    val endDate: Instant,
)

data class SleepLogResponseDTO(
    val interval: IntervalDTO,
    val quality: SleepQuality,
)

data class SleepLogResponseDataDTO(
    val interval: IntervalDTO,
    val avgTotalTimeInBed: SimpleTime,
    val avgTimeGetsToBed: SimpleTime,
    val avgTimeGetsOutOfBed: SimpleTime,
    val sleepQualities: Map<SleepQuality, Int>,
)

data class SimpleTime(
    val minutes: Int,
    val hours: Int,
)
