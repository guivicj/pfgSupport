package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.AdminDTO
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.AdminMapper
import com.guivicj.apiSupport.repositories.AdminRepository
import com.guivicj.apiSupport.repositories.UserRepository
import org.apache.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val adminRepository: AdminRepository,
    private val adminMapper: AdminMapper,
    private val userRepository: UserRepository
) {

    fun getAllAdmins(): List<AdminDTO> =
        adminRepository.findAll().map(adminMapper::toDTO)

    fun getAdminById(id: Long): AdminDTO =
        adminMapper.toDTO(
            adminRepository.findById(id)
                .orElseThrow { RuntimeException("Admin not found with ID: $id") })

    fun getAdminByUserId(userId: Long): AdminDTO =
        adminMapper.toDTO(
            adminRepository.getAdminByUserId(userId)
        )

    @Transactional
    fun addAdmin(currentUser: UserSessionInfoDTO, dto: AdminDTO): AdminDTO {
        if (currentUser.user.type != UserType.ADMIN) {
            throw RuntimeException("Only ADMINs can create other admins")
        }

        val user = userRepository.findByEmail(currentUser.user.email)
            .orElseThrow { RuntimeException("User not found with Email: ${currentUser.user.email}") }

        val admin = adminMapper.toEntity(AdminDTO(userId = user.id))
        adminRepository.save(admin)

        return adminMapper.toDTO(admin)
    }

    @Transactional
    fun deleteAdmin(currentUser: UserSessionInfoDTO, dto: AdminDTO): Response {
        if (currentUser.user.type != UserType.ADMIN) {
            throw RuntimeException("Only ADMINs can delete other admins")
        }

        val userToDelete = userRepository.findById(dto.userId)
            .orElseThrow { RuntimeException("Target user not found with ID: ${dto.userId}") }

        if (userToDelete.type != UserType.ADMIN) {
            throw RuntimeException("Target user is not an admin")
        }

        if (!adminRepository.existsById(userToDelete.id)) {
            throw RuntimeException("Admin entity not found for user ID: ${userToDelete.id}")
        }

        adminRepository.deleteById(userToDelete.id)

        userToDelete.type = UserType.USER
        userRepository.save(userToDelete)

        return Response(HttpStatus.SC_OK, "Removed admin role from user ${userToDelete.email}")
    }
}
