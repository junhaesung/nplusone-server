package com.haeseong.nplusone.domain.item.image

import com.haeseong.nplusone.domain.item.Item
import com.haeseong.nplusone.domain.item.StoreType
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class ItemImage(
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val itemImageId: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "itemId")
    val item: Item,
    val imageUrl: String?,
    val storeType: StoreType,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime
}