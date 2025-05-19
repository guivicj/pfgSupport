package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.services.DeviceTokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/device-tokens")
class DeviceTokenController(
    private val deviceTokenService: DeviceTokenService
) {
    @PutMapping("/{userId}")
    fun registerToken(
        @PathVariable userId: Long,
        @RequestBody token: String
    ): ResponseEntity<Void> {
        deviceTokenService.registerToken(userId, token)
        return ResponseEntity.ok().build()
    }
}
