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
