package org.guivicj.support.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserType {
    ADMIN, TECHNICIAN, USER
}