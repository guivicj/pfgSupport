package org.guivicj.support.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.guivicj.support.R
import org.guivicj.support.data.repository.AuthRepositoryImpl

class RealTimeMessagingService(
) : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification.let {
            sendNotification(it!!.title ?: "Title", it.body ?: "Body")
        }
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val client = HttpClient() {
                        install(ContentNegotiation) {
                            json(Json {
                                prettyPrint = true
                                isLenient = true
                            })
                        }
                    }
                    val authRepository = AuthRepositoryImpl(client)
                    authRepository.saveFcmToken(userId.toLong(), token)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun sendNotification(title: String, body: String) {
        val channelId = "chat_messages"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Chat Messages",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .build()
        manager.notify(0, notification)
    }
}
