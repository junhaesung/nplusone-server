package com.haeseong.nplusone.domain.notification

data class NotificationRequestVo(
    val title: String,
    val content: String,
    val fcmToken: String,
)
