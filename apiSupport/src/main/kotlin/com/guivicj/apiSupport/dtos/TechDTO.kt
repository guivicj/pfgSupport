package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.TechnicianType

data class TechDTO(
    val id: Long,
    var userId: Long,
    var technicianType: TechnicianType
)
