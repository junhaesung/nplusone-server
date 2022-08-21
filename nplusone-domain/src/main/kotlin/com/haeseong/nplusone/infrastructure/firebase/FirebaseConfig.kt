package com.haeseong.nplusone.infrastructure.firebase

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI

@Configuration
class FirebaseConfig {

    @Bean
    fun firebaseProperties() = FirebaseProperties()

    @Bean
    fun firebaseApp(firebaseProperties: FirebaseProperties): FirebaseApp {
        val options = FirebaseOptions.builder()
            .setCredentials(
                ServiceAccountCredentials.newBuilder()
                    .setProjectId(firebaseProperties.projectId)
                    .setPrivateKeyId(firebaseProperties.privateKeyId)
                    .setPrivateKeyString(firebaseProperties.privateKey)
                    .setClientEmail(firebaseProperties.clientEmail)
                    .setClientId(firebaseProperties.clientId)
                    .setTokenServerUri(URI.create(firebaseProperties.tokenUri))
                    .build()
            )
            .build()
        return FirebaseApp.initializeApp(options)
    }
}