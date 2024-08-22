package com.noom.interview.fullstack.sleep.controllers

import com.noom.interview.fullstack.sleep.models.dtos.SleepLogRequestDTO
import com.noom.interview.fullstack.sleep.models.dtos.SleepLogResponseDTO
import com.noom.interview.fullstack.sleep.models.dtos.SleepLogResponseDataDTO
import com.noom.interview.fullstack.sleep.services.SleepService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sleep")
class SleepController {
    @Autowired
    private lateinit var sleepService: SleepService

    @PostMapping
    fun create(
        @RequestBody request: SleepLogRequestDTO,
        @RequestHeader(value = "x-external-id") externalId: String,
    ): ResponseEntity<Void> {
        sleepService
            .createSleepLog(externalId, request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping(value = ["/last-night"])
    fun lastNight(
        @RequestHeader(value = "x-external-id") externalId: String,
    ): ResponseEntity<SleepLogResponseDTO> {
        val sleepLog = sleepService.getLastNightSleepLog(externalId)
        return ResponseEntity.ok().body(sleepLog)
    }

    @GetMapping(value = ["/interval"])
    fun interval(
        @RequestHeader(value = "x-external-id") externalId: String,
    ): ResponseEntity<SleepLogResponseDataDTO> {
        val sleepLog = sleepService.getSleepLogDataFromLastNDays(externalId)
        return ResponseEntity.ok().body(sleepLog)
    }
}
