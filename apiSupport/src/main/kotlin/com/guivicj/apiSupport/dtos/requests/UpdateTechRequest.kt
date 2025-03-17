package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.TechnicianType

data class UpdateTechRequest(
    val userid: Long,
    val technicianType: TechnicianType
)