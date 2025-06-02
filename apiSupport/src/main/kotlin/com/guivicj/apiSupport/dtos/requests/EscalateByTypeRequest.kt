package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.TechnicianType

data class EscalateByTypeRequest(
    val technicianType: TechnicianType
)
