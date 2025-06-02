package org.guivicj.support.domain.model

import kotlinx.serialization.Serializable
import org.guivicj.support.data.model.TechnicianType

@Serializable
data class TechnicianDTO(
    val id: Long,
    val userId: Long,
    val technicianType: TechnicianType
)

