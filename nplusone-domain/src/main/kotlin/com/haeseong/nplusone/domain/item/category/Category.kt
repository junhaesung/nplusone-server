package com.haeseong.nplusone.domain.item.category

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@EntityListeners(AuditingEntityListener::class)
class Category(
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val categoryId: Long = 0L,
    val name: String,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    companion object {
        fun from(categoryCreateVo: CategoryCreateVo): Category {
            return Category(
                name = categoryCreateVo.name
            )
        }
    }
}
