package org.guivicj.support.fcm

import com.google.firebase.messaging.FirebaseMessaging

class FcmManager {
    fun getToken(onTokenReceiver: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                onTokenReceiver(token)
            } else {
                onTokenReceiver(null)
            }
        }
    }
}