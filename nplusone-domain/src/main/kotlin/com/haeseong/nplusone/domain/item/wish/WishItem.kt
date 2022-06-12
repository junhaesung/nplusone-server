package com.haeseong.nplusone.domain.item.wish

import com.haeseong.nplusone.domain.item.Item
import com.haeseong.nplusone.domain.member.Member
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class WishItem (
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val wishItemId: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "memberId")
    val member: Member,
    @ManyToOne
    @JoinColumn(name = "itemId")
    val item: Item,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WishItem

        if (wishItemId != other.wishItemId) return false

        return true
    }

    override fun hashCode(): Int {
        return wishItemId.hashCode()
    }
}
