package com.noom.interview.fullstack.sleep.services.stubs

import com.noom.interview.fullstack.sleep.models.dtos.SleepLogDTO
import com.noom.interview.fullstack.sleep.models.entities.User
import com.noom.interview.fullstack.sleep.models.enums.SleepQuality
import com.noom.interview.fullstack.sleep.utils.yesterday
import java.time.Instant
import java.time.temporal.ChronoUnit

val singleSleepLogsStub =
    listOf(
        SleepLogDTO(
            id = 1,
            user = User(1, "id"),
            startDate = yesterday.plus(16, ChronoUnit.HOURS),
            endDate = yesterday.plus(27, ChronoUnit.HOURS),
            quality = SleepQuality.OK,
            createdAt = Instant.now(),
        ),
    )

val multipleSleepLogs =
    (1..30).map { n ->
        val start = Instant.parse("2024-08-21T21:32:00Z").minus(30, ChronoUnit.DAYS).plus(n.toLong(), ChronoUnit.DAYS)
        SleepLogDTO(
            id = 1,
            user = User(1, "id"),
            startDate = start,
            endDate =
                start
                    .plus(8L, ChronoUnit.HOURS),
            quality = getSleepQuality(n),
            createdAt = start.plus(12L, ChronoUnit.HOURS),
        )
    }

private fun getSleepQuality(n: Int): SleepQuality {
    if (n < 10) return SleepQuality.GOOD
    if (n < 20) return SleepQuality.OK
    return SleepQuality.BAD
}
