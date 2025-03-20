package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.TechDTO
import com.guivicj.apiSupport.dtos.requests.DeleteEmployeeRequest
import com.guivicj.apiSupport.dtos.requests.EmployeeRequest
import com.guivicj.apiSupport.dtos.requests.UpdateTechRequest
import com.guivicj.apiSupport.dtos.requests.UserUpdateRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.TechMapper
import com.guivicj.apiSupport.repositories.TechRepository
import com.guivicj.apiSupport.repositories.UserRepository
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class TechService(
    private val techRepository: TechRepository,
    private val techMapper: TechMapper,
    private val userRepository: UserRepository,
    private val userService: UserService,
) {

    fun getAllTechs(): List<TechDTO> =
        techRepository.findAll().map(techMapper::toDTO)

    fun getTechById(id: Long): TechDTO? =
        techRepository.findById(id).map(techMapper::toDTO).orElse(null)

    fun getTechsByType(type: TechnicianType): List<TechDTO> =
        techRepository.getTechsByTechnicianType(type).map(techMapper::toDTO)

    @Transactional
    fun addTech(employeeRequest: EmployeeRequest): TechDTO {
        val user =
            userRepository.findById(employeeRequest.userid)
                .orElseThrow { throw RuntimeException("User not found") }
        if (user.type != UserType.TECHNICIAN) throw RuntimeException("User is not TECHNICIAN")
        val tech = techMapper.toEntity(TechDTO(userId = user.id, technicianType = TechnicianType.JUNIOR))
        techRepository.save(tech)

        return techMapper.toDTO(tech)
    }

    @org.springframework.transaction.annotation.Transactional
    fun updateTech(techRequest: UpdateTechRequest): TechDTO {
        val user = userRepository.findById(techRequest.userid)
            .orElseThrow { RuntimeException("User not found with ID: ${techRequest.userid}") }

        if (user.type != UserType.TECHNICIAN) {
            throw RuntimeException("User is not TECHNICIAN")
        }

        val tech = techRepository.findById(user.id)
            .orElseThrow { RuntimeException("Tech not found") }

        tech.technicianType = techRequest.technicianType
        techRepository.save(tech)

        return techMapper.toDTO(tech)
    }

    @Transactional
    fun deleteTech(techRequest: DeleteEmployeeRequest): Response {
        val user = userRepository.findById(techRequest.id)
            .orElseThrow { RuntimeException("User not found") }

        if (user.type != UserType.TECHNICIAN) {
            throw RuntimeException("User is not TECHNICIAN")
        }

        if (!techRepository.existsById(user.id)) {
            throw RuntimeException("Tech not found")
        }

        if (techRequest.userType != UserType.ADMIN) {
            return Response(HttpStatus.SC_UNAUTHORIZED, "Only Admin is allowed to delete technicians")
        }

        techRepository.deleteById(user.id)

        user.type = UserType.USER
        userRepository.save(user)

        return Response(HttpStatus.SC_OK, "Successfully removed technician role. User remains as a normal user.")
    }

}