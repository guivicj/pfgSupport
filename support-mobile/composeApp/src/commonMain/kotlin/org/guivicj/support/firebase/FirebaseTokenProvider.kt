package org.guivicj.support.firebase

interface FirebaseTokenProvider {
    suspend fun getIdToken(): String
}