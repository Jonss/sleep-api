package com.noom.interview.fullstack.sleep.services

import com.noom.interview.fullstack.sleep.exceptions.EntityNotFoundException
import com.noom.interview.fullstack.sleep.models.dtos.IntervalDTO
import com.noom.interview.fullstack.sleep.models.dtos.SleepLogDTO
import com.noom.interview.fullstack.sleep.models.dtos.SleepLogRequestDTO
import com.noom.interview.fullstack.sleep.models.dtos.SleepLogResponseDTO
import com.noom.interview.fullstack.sleep.models.dtos.SleepLogResponseDataDTO
import com.noom.interview.fullstack.sleep.models.entities.SleepLog
import com.noom.interview.fullstack.sleep.models.enums.SleepQuality
import com.noom.interview.fullstack.sleep.repositories.SleepLogRepository
import com.noom.interview.fullstack.sleep.repositories.UserRepository
import com.noom.interview.fullstack.sleep.utils.endOfDay
import com.noom.interview.fullstack.sleep.utils.startOfDay
import com.noom.interview.fullstack.sleep.utils.yesterday
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

const val PAST_DAYS = 30L

@Service
class SleepService {
    @Autowired
    private lateinit var sleepLogRepository: SleepLogRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    fun createSleepLog(
        userId: String,
        sleepLogDTO: SleepLogRequestDTO,
    ) {
        val user = userRepository.findUserByExternalId(userId) ?: throw EntityNotFoundException("user")

        val sleepLog =
            SleepLog(
                id = 0,
                userId = user.id,
                startDate = sleepLogDTO.startDate,
                endDate = sleepLogDTO.endDate,
                quality = sleepLogDTO.quality,
                createdAt = Instant.now(),
            )
        sleepLogRepository.create(sleepLog)
    }

    fun getLastNightSleepLog(userId: String): SleepLogResponseDTO? {
        val user = userRepository.findUserByExternalId(userId) ?: throw EntityNotFoundException("user")

        val from = startOfDay(yesterday)
        val to = endOfDay(yesterday)

        val sleepLogs = sleepLogRepository.fetchByUserIdFromInterval(user.id, from, to)
        if (sleepLogs.isEmpty()) return null
        val sleepLog = sleepLogs[0]
        println(sleepLog)
        return SleepLogResponseDTO(
            interval =
                IntervalDTO(
                    startDate = sleepLog.startDate,
                    endDate = sleepLog.endDate,
                ),
            quality = sleepLog.quality,
        )
    }

    fun getSleepLogDataFromLastNDays(
        userId: String,
        days: Long = PAST_DAYS,
    ): SleepLogResponseDataDTO? {
        val user = userRepository.findUserByExternalId(userId) ?: throw EntityNotFoundException("user")

        val thirtyDaysAgo = Instant.now().minus(days, ChronoUnit.DAYS)
        val from = startOfDay(thirtyDaysAgo)
        val to = endOfDay(Instant.now())

        val sleepLogs = sleepLogRepository.fetchByUserIdFromInterval(user.id, from, to)

        return SleepLogResponseDataDTO(
            interval = IntervalDTO(startDate = from, endDate = to),
            avgTimeGetsToBed =
                calculateAverageDates(
                    sleepLogs.map { it.startDate },
                ),
            avgTotalTimeInBed = getAvgTotalTimeInBed(sleepLogs),
            avgTimeGetsOutOfBed =
                calculateAverageDates(
                    sleepLogs.map { it.endDate },
                ),
            sleepQualities = getSleepQualities(sleepLogs),
        )
    }

    private fun getSleepQualities(sleepLogs: List<SleepLogDTO>): Map<SleepQuality, Int> =
        sleepLogs
            .map { it.quality }
            .groupingBy { it }
            .eachCount()

    private fun getAvgTotalTimeInBed(sleepLogs: List<SleepLogDTO>): String {
        val totalSeconds =
            getTotalSeconds(sleepLogs)

        try {
            val avgSeconds = totalSeconds / sleepLogs.size
            val hours = avgSeconds / 3600
            val minutes = (avgSeconds % 3600) / 60

            return "$hours h $minutes min"
        } catch (e: ArithmeticException) {
            return ""
        }
    }

    fun calculateAverageDates(dates: List<Instant>): String {
        if (dates.isEmpty()) return ""

        val epochSeconds = dates.map { it.epochSecond }

        val averageEpochSeconds = epochSeconds.average().toLong()

        val averageInstant = Instant.ofEpochSecond(averageEpochSeconds)
        val time = LocalTime.ofInstant(averageInstant, ZoneId.systemDefault())

        return "${time.hour} h ${time.minute} min"
    }

    private fun getTotalSeconds(sleepLogs: List<SleepLogDTO>): Long {
        val totalSeconds =
            sleepLogs.sumOf { l -> Duration.between(l.startDate, l.endDate).toSeconds() }
        return totalSeconds
    }
}
