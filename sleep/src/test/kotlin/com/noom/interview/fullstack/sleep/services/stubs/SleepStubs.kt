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
