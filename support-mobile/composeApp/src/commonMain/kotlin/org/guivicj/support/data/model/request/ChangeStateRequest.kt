package org.guivicj.support.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChangeStateRequest(
    val state: String,
    val technicianId: Long
)
