package com.haeseong.nplusone.infrastructure.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@Disabled
@SpringBootTest
class FirebaseTests {

    @Test
    fun send() {
        val registrationToken = "dn4WGlzrQBi4P6sgYjIyWa:APA91bHVhC5Qy9USE4K6Nnt06D4wDgfbNaxH_O3m6j40wN3AjHzbIOJVSt5BEYELSUkxxWB6TOGGCSnSkha0_NAii_ROF9b2TUX3DHalp7hdFHOSm9QoieVN9jlHA-QLlElqi_MsCWXs";
        val message: Message = Message.builder()
            .putData("score", "850")
            .putData("time", "2:45")
            .setToken(registrationToken)
            .build()
        val response = FirebaseMessaging.getInstance().send(message)
        println(response)
    }
}