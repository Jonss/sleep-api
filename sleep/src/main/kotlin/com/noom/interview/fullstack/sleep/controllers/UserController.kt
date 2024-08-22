package com.noom.interview.fullstack.sleep.controllers

import com.noom.interview.fullstack.sleep.models.dtos.UserResponseDTO
import com.noom.interview.fullstack.sleep.models.entities.User
import com.noom.interview.fullstack.sleep.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @PostMapping
    fun create(): ResponseEntity<UserResponseDTO> {
        val user = User(0, UUID.randomUUID().toString())
        userRepository.create(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO(user.externalId))
    }
}
