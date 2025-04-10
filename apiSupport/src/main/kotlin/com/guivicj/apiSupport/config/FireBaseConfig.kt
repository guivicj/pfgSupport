package com.guivicj.apiSupport.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileInputStream

@Configuration
class FirebaseConfig {

    @Value("\${firebase.credentials.path}")
    lateinit var credentialsPath: String

    @Bean
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
}
