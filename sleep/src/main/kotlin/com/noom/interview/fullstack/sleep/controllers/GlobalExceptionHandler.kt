package com.noom.interview.fullstack.sleep.controllers

import com.noom.interview.fullstack.sleep.exceptions.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(
        ex: EntityNotFoundException,
        request: WebRequest?,
    ): ResponseEntity<Any> {
        val errorMessage = ex.message
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }
}
