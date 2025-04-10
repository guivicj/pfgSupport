package org.guivicj.support.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseLoginRequest(val token: String)
