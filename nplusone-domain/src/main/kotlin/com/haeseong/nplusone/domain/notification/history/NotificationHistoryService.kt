package com.haeseong.nplusone.domain.notification.history

import com.haeseong.nplusone.domain.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NotificationHistoryService {
    fun createSuccess(member: Member): NotificationHistory
    fun createFailure(member: Member): NotificationHistory
}

@Service
@Transactional
class NotificationHistoryServiceImpl(
    private val notificationHistoryRepository: NotificationHistoryRepository
) : NotificationHistoryService {
    @Transactional(readOnly = false)
    override fun createSuccess(member: Member): NotificationHistory {
        return notificationHistoryRepository.save(
            NotificationHistory.success(member)
        )
    }

    @Transactional(readOnly = false)
    override fun createFailure(member: Member): NotificationHistory {
        return notificationHistoryRepository.save(
            NotificationHistory.failure(member)
        )
    }
}
