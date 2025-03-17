package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.TechnicianType

data class TechDTO(
    var userId: Long,
    var technicianType: TechnicianType
)
