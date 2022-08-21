package com.haeseong.nplusone.domain.notification.history

import org.springframework.data.jpa.repository.JpaRepository

interface NotificationHistoryRepository : JpaRepository<NotificationHistory, Long> {

}
