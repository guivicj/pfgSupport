package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.TechDTO
import com.guivicj.apiSupport.dtos.requests.TechRequest
import com.guivicj.apiSupport.dtos.requests.UpdateTechRequest
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.TechMapper
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
}