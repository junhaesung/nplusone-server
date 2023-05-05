package com.haeseong.nplusone.domain.item.category

import com.haeseong.nplusone.domain.item.Item
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
@EntityListeners(AuditingEntityListener::class)
class ItemCategory(
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val itemCategoryId: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "itemId")
    val item: Item,
    @ManyToOne
    @JoinColumn(name = "categoryId")
    val category: Category,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    companion object {
        fun of(item: Item, category: Category): ItemCategory {
            return ItemCategory(item = item, category = category)
        }
    }
}
