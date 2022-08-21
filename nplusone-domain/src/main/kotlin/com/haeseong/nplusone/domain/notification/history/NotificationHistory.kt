package com.haeseong.nplusone.domain.notification.history

import com.haeseong.nplusone.domain.member.Member
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class NotificationHistory(
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val notificationHistoryId: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    val member: Member,

    @Enumerated(EnumType.STRING)
    val notificationStatus: NotificationStatus,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    companion object {
        fun success(member: Member) = NotificationHistory(
            member = member,
            notificationStatus = NotificationStatus.SUCCESS,
        )
        fun failure(member: Member) = NotificationHistory(
            member = member,
            notificationStatus = NotificationStatus.FAILURE,
        )
    }
}