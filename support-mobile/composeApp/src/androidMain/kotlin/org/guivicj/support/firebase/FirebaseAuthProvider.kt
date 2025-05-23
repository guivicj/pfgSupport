package org.guivicj.support.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseAuthProvider : FirebaseTokenProvider {
    override suspend fun getIdToken(): String {
        return FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.await()?.token!!
    }

}

fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.getIdToken(true)?.addOnSuccessListener {
                    val token = it.token
                    onResult(true, token)
                }
            } else {
                onResult(false, task.exception?.message)
            }
        }
}

