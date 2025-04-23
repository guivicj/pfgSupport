package org.guivicj.support.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class StateType {
    OPEN, IN_PROGRESS, CLOSED
}