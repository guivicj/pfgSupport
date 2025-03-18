package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.TechDTO
import com.guivicj.apiSupport.dtos.requests.DeleteTechRequest
import com.guivicj.apiSupport.dtos.requests.TechRequest
import com.guivicj.apiSupport.dtos.requests.UpdateTechRequest
import com.guivicj.apiSupport.dtos.responses.DeleteResponse
import com.guivicj.apiSupport.dtos.responses.DeleteTechResponse
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.TechMapper
import com.guivicj.apiSupport.models.Technician
import com.guivicj.apiSupport.repositories.TechRepository
import com.guivicj.apiSupport.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TechService(
    private val techRepository: TechRepository,
    private val techMapper: TechMapper,
    private val userRepository: UserRepository,
) {

    fun getAllTechs(): List<TechDTO> {
        return techRepository.findAll().stream().map(techMapper::toDTO).toList()
    }

    fun getTechById(id: Long): Optional<TechDTO> {
        return techRepository.findById(id).map(techMapper::toDTO)
    }

    fun getTechsByType(type: TechnicianType): List<TechDTO> {
        return techRepository.getTechsByTechnicianType(type)
            .map(techMapper::toDTO)
    }

    fun addTech(techRequest: TechRequest): TechDTO {
        val user = userRepository.findById(techRequest.userid).orElseThrow { throw RuntimeException("User not found") }
        if (user.type != UserType.TECHNICIAN) throw RuntimeException("User is not TECHNICIAN")
        val tech = TechDTO(
            userId = user.id,
            technicianType = TechnicianType.JUNIOR
        )
        techRepository.save(techMapper.toEntity(tech))
        return tech
    }

    fun updateTech(techRequest: UpdateTechRequest): TechDTO {
        val user = userRepository.findById(techRequest.userid).orElseThrow { throw RuntimeException("User not found") }
        if (user.type != UserType.TECHNICIAN) throw RuntimeException("User is not TECHNICIAN")
        val tech = TechDTO(
            userId = user.id,
            technicianType = techRequest.technicianType
        )
        techRepository.save(techMapper.toEntity(tech))

        return tech
    }

    fun deleteTech(techRequest: DeleteTechRequest): DeleteTechResponse {
        val user = userRepository.findById(techRequest.id).orElseThrow { throw RuntimeException("User not found") }
        if (user.type != UserType.TECHNICIAN) throw RuntimeException("User is not TECHNICIAN")
        val tech = techRepository.findById(user.id).orElseThrow { throw RuntimeException("User not found") }
        var isDeleted = false
        if (techRequest.userType == UserType.ADMIN) {
            techRepository.delete(tech)
            isDeleted = true
        }
        return if (isDeleted) {
            DeleteTechResponse(200, "Successfully deleted the user")
        } else {
            DeleteTechResponse(403, "Only Admin is allowed to delete users")
        }
    }
}