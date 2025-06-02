package org.guivicj.support.data.model.request

import kotlinx.serialization.Serializable
import org.guivicj.support.data.model.TechnicianType

@Serializable
data class EscalateByTypeRequest(
    val technicianType: TechnicianType
)
