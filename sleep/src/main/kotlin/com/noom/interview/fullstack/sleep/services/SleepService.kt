package com.noom.interview.fullstack.sleep.services

import com.noom.interview.fullstack.sleep.models.dtos.IntervalDTO
import com.noom.interview.fullstack.sleep.models.dtos.SleepLogResponseDTO
import com.noom.interview.fullstack.sleep.repositories.SleepLogRepository
import com.noom.interview.fullstack.sleep.utils.startOfDay
import com.noom.interview.fullstack.sleep.utils.yesterday
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SleepService {
    @Autowired
    private lateinit var sleepLogRepository: SleepLogRepository

    fun getLastNightSleepLog(userId: Long): SleepLogResponseDTO? {
        val from = startOfDay(yesterday)
        val to = startOfDay(yesterday)
        val sleepLogs = sleepLogRepository.fetchByUserIdFromInterval(userId, from, to)
        if (sleepLogs.isEmpty()) return null
        val sleepLog = sleepLogs[0]
        return SleepLogResponseDTO(
            interval =
                IntervalDTO(
                    startDate = sleepLog.startDate,
                    endDate = sleepLog.endDate,
                ),
            quality = sleepLog.quality,
        )
    }
}
