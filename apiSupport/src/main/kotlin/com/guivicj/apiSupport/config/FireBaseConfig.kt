package com.guivicj.apiSupport.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileInputStream
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfig {

    @Value("\${firebase.credentials.path}")
    lateinit var credentialsPath: String

    @PostConstruct
    fun firebaseApp(): FirebaseApp {
        val serviceAccount = File(credentialsPath)

        if (!serviceAccount.exists()) {
            throw RuntimeException("Credentials file not found in $credentialsPath")
        }

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(FileInputStream(serviceAccount)))
            .build()

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun firebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}
