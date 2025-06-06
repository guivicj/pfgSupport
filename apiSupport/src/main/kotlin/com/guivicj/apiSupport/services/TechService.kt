package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.TechDTO
import com.guivicj.apiSupport.dtos.TechnicianStatsDTO
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.StateType
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.TechMapper
import com.guivicj.apiSupport.repositories.TechRepository
import com.guivicj.apiSupport.repositories.TicketRepository
import com.guivicj.apiSupport.repositories.UserRepository
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class TechService(
    private val techRepository: TechRepository,
    private val techMapper: TechMapper,
    private val userRepository: UserRepository,
    private val ticketRepository: TicketRepository
) {

    fun getAllTechs(): List<TechDTO> =
        techRepository.findAll().map(techMapper::toDTO)

    fun getTechById(id: Long): TechDTO? =
        techRepository.findById(id).map(techMapper::toDTO).orElse(null)

    fun getTechsByType(type: TechnicianType): List<TechDTO> =
        techRepository.getTechsByTechnicianType(type).map(techMapper::toDTO)

    fun getTechnicianIdByUserId(userId: Long): Long? {
        return techRepository.findByUserModel_Id(userId)?.id
    }

    @Transactional
    fun addTech(currentUser: UserSessionInfoDTO, dto: TechDTO): TechDTO {
        if (currentUser.user.type != UserType.ADMIN) {
            throw RuntimeException("Only ADMINs can create technicians")
        }

        val user = userRepository.findById(dto.userId)
            .orElseThrow { RuntimeException("User to promote not found") }

        user.type = UserType.TECHNICIAN
        userRepository.save(user)

        val tech = techMapper.toEntity(dto)
        techRepository.save(tech)

        return techMapper.toDTO(tech)
    }

    @Transactional
    fun updateTech(currentUser: UserSessionInfoDTO, dto: TechDTO): TechDTO {
        if (currentUser.user.type != UserType.ADMIN) {
            throw RuntimeException("Only ADMINs can update technicians")
        }

        val user = userRepository.findById(dto.userId)
            .orElseThrow { RuntimeException("User not found with ID: ${dto.userId}") }

        if (user.type != UserType.TECHNICIAN || !techRepository.existsById(user.id)) {
            throw RuntimeException("User is not a valid registered TECHNICIAN")
        }

        val tech = techRepository.findById(user.id)
            .orElseThrow { RuntimeException("Tech not found") }

        tech.technicianType = dto.technicianType
        techRepository.save(tech)

        return techMapper.toDTO(tech)
    }


    @Transactional
    fun deleteTech(currentUser: UserSessionInfoDTO, dto: TechDTO): Response {
        if (currentUser.user.type != UserType.ADMIN) {
            throw RuntimeException("Only ADMINs can delete other admins")
        }

        val user = userRepository.findById(dto.userId)
            .orElseThrow { RuntimeException("User not found") }

        if (user.type != UserType.TECHNICIAN) {
            throw RuntimeException("User is not TECHNICIAN")
        }

        if (!techRepository.existsById(user.id)) {
            throw RuntimeException("Tech not found")
        }

        techRepository.deleteById(user.id)

        user.type = UserType.USER
        userRepository.save(user)

        return Response(HttpStatus.SC_OK, "Successfully removed technician role. User remains as a normal user.")
    }

    fun getTechnicianStats(techId: Long): TechnicianStatsDTO {
        val tech = techRepository.findById(techId).orElseThrow { RuntimeException("Technician not found") }
        val tickets = ticketRepository.getTicketsByTechnicianId(tech)

        val closed = tickets.filter { it.state == StateType.CLOSED }
        val inProgress = tickets.filter { it.state == StateType.IN_PROGRESS }

        val avgResolutionMinutes = closed.mapNotNull {
            if (it.closedAt != null)
                Duration.between(it.openedAt, it.closedAt).toMinutes()
            else null
        }.average().toLong()

        val avgTimeFormated = formatResolutionTime(avgResolutionMinutes)

        return TechnicianStatsDTO(
            technicianId = techId,
            totalTickets = tickets.size,
            totalResolved = closed.size,
            totalInProgress = inProgress.size,
            avgResolutionTime = avgTimeFormated
        )
    }

    private fun formatResolutionTime(avgResolutionMinutes: Long): String {
        val duration = Duration.ofMinutes(avgResolutionMinutes)
        val hours = duration.toHours()
        val minutes = duration.minusHours(hours).toMinutes()
        val formattedDuration = String.format("%02d:%02d", hours, minutes)
        return formattedDuration
    }
}
