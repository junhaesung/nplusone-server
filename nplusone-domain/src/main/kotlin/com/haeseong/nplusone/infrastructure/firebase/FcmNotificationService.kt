package com.haeseong.nplusone.infrastructure.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.haeseong.nplusone.domain.notification.NotificationRequestVo
import com.haeseong.nplusone.domain.notification.NotificationResponseVo
import com.haeseong.nplusone.domain.notification.NotificationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FcmNotificationService : NotificationService {

    override fun send(notificationRequestVo: NotificationRequestVo): NotificationResponseVo {
        try {
            val result = FirebaseMessaging.getInstance().send(
                Message.builder()
                    .setNotification(
                        Notification.builder()
                            .setTitle(notificationRequestVo.title)
                            .setBody(notificationRequestVo.content)
                            .build()
                    )
                    .setToken(notificationRequestVo.fcmToken)
                    .build()
            )
            log.info("result: $result")
            return NotificationResponseVo(
                isSuccess = true,
                message = result,
            )
        } catch (e: Exception) {
            log.error("Failed to send FCM notification", e)
            return NotificationResponseVo(
                isSuccess = false,
                message = e.message ?: "Failed to send FCM notification",
            )
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(FcmNotificationService::class.java)
    }
}