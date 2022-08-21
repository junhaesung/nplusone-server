package com.haeseong.nplusone.infrastructure.firebase

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("nplusone.notification.firebase")
class FirebaseProperties {
    lateinit var projectId: String
    lateinit var privateKeyId: String
    lateinit var privateKey: String
    lateinit var clientEmail: String
    lateinit var clientId: String
    lateinit var tokenUri: String
}