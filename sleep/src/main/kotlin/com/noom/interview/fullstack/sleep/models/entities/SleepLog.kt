package com.noom.interview.fullstack.sleep.models.entities

import com.noom.interview.fullstack.sleep.models.enums.SleepQuality
import java.time.Instant

data class SleepLog(
    val id: Long,
    val userId: Long,
    val startDate: Instant,
    val endDate: Instant,
    val quality: SleepQuality,
    val createdAt: Instant,
)
