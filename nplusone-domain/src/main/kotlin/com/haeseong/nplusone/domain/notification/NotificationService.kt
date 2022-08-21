package com.haeseong.nplusone.domain.notification

interface NotificationService {
    fun send(notificationRequestVo: NotificationRequestVo): NotificationResponseVo
}