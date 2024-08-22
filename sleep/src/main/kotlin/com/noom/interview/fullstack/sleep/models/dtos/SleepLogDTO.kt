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

data class SleepLogResponseDTO(
    val interval: IntervalDTO,
    val quality: SleepQuality,
)

data class IntervalDTO(
    val startDate: Instant,
    val endDate: Instant,
)

data class SleepLogResponseDataDTO(
    val interval: IntervalDTO,
    val avgTotalTimeInBed: String,
    val avgTimeGetsToBed: String,
    val avgTimeGetsOutOfBed: String,
    val sleepQualities: Map<SleepQuality, Int>,
)
