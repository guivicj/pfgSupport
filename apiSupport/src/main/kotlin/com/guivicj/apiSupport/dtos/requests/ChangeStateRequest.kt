package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.StateType

data class ChangeStateRequest(
    val technicianId: Long,
    val state: StateType,
)