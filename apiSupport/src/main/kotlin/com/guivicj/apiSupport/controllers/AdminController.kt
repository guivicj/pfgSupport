package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.annotations.CurrentUser
import com.guivicj.apiSupport.annotations.RoleRequired
import com.guivicj.apiSupport.dtos.AdminDTO
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.services.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(val adminService: AdminService) {
    @GetMapping
    fun getAllAdmins(): List<AdminDTO> {
        return adminService.getAllAdmins()
    }

    @GetMapping("/id/{id}")
    fun getAdminById(@PathVariable id: Long): ResponseEntity<AdminDTO> {
        val admin = adminService.getAdminById(id)
        return ResponseEntity.ok(admin)
    }

    @GetMapping("/userid/{id}")
    fun getAdminByUserId(@PathVariable id: Long): ResponseEntity<AdminDTO> {
        val admin = adminService.getAdminByUserId(id)
        return ResponseEntity.ok(admin)
    }

    @RoleRequired(UserType.ADMIN)
    @PostMapping
    fun addAdmin(
        @CurrentUser user: UserSessionInfoDTO,
        @RequestBody dto: AdminDTO
    ): ResponseEntity<Any> {
        val admin = adminService.addAdmin(user, dto)
        return ResponseEntity.ok(admin)
    }

    @RoleRequired(UserType.ADMIN)
    @DeleteMapping
    fun deleteAdmin(
        @CurrentUser user: UserSessionInfoDTO,
        @RequestBody dto: AdminDTO
    ): ResponseEntity<Any> {
        val response = adminService.deleteAdmin(user, dto)
        return ResponseEntity.status(response.status).body(response)
    }
}
