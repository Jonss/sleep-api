package com.noom.interview.fullstack.sleep.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

val yesterday =
    LocalDateTime
        .of(2024, 8, 21, 21, 32)
        .atZone(ZoneOffset.UTC)
        .minus(1, ChronoUnit.DAYS)
        .toInstant()

fun startOfDay(instant: Instant) = LocalDate.ofInstant(instant, ZoneId.systemDefault()).atStartOfDay().toInstant(ZoneOffset.UTC)

fun endOfDay(instant: Instant) = LocalDate.ofInstant(yesterday, ZoneId.systemDefault()).atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)
