package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.AdminDTO
import com.guivicj.apiSupport.dtos.requests.DeleteEmployeeRequest
import com.guivicj.apiSupport.dtos.requests.EmployeeRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.services.AdminService
import org.springframework.http.HttpStatus
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

    @PostMapping
    fun addAdmin(@RequestBody adminRequest: EmployeeRequest): ResponseEntity<AdminDTO> {
        val admin = adminService.addAdmin(adminRequest)
        return ResponseEntity.ok(admin)
    }

    @DeleteMapping("/delete")
    fun deleteAdmin(@RequestBody adminRequest: DeleteEmployeeRequest): ResponseEntity<Response> {
        val response = adminService.deleteAdmin(adminRequest)
        return ResponseEntity.status(response.status).body(response)
    }
}