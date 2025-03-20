package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.AdminDTO
import com.guivicj.apiSupport.dtos.requests.DeleteEmployeeRequest
import com.guivicj.apiSupport.dtos.requests.EmployeeRequest
import com.guivicj.apiSupport.dtos.responses.Response
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
    fun addAdmin(adminRequest: EmployeeRequest): AdminDTO {
        val user = userRepository.findById(adminRequest.userid)
            .orElseThrow { RuntimeException("User not found with ID: ${adminRequest.userid}") }

        if (user.type != UserType.ADMIN) {
            throw RuntimeException("User is not an ADMIN")
        }

        val admin = adminMapper.toEntity(AdminDTO(userId = user.id))
        adminRepository.save(admin)

        return adminMapper.toDTO(admin)
    }

    @Transactional
    fun deleteAdmin(adminRequest: DeleteEmployeeRequest): Response {
        val user = userRepository.findById(adminRequest.id)
            .orElseThrow { RuntimeException("User not found with ID: ${adminRequest.id}") }

        if (user.type != UserType.ADMIN) {
            throw RuntimeException("User is not an ADMIN")
        }

        if (!adminRepository.existsById(user.id)) {
            throw RuntimeException("Admin not found for user ID: ${user.id}")
        }

        if (adminRequest.userType != UserType.ADMIN) {
            return Response(HttpStatus.SC_UNAUTHORIZED, "Only Admin is allowed to delete administrators")
        }

        adminRepository.deleteById(user.id)

        user.type = UserType.USER
        userRepository.save(user)

        return Response(HttpStatus.SC_OK, "Successfully removed admin role. User remains as a normal user.")
    }
}
