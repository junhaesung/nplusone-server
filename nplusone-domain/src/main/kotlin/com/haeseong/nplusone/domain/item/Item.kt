package com.haeseong.nplusone.domain.item

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Item(
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val itemId: Long = 0L,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    @Enumerated(EnumType.STRING)
    val discountType: DiscountType,
    @Enumerated(EnumType.STRING)
    val storeType: StoreType,
    @Column(name = "itemYearMonth")
    val yearMonth: YearMonth,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (itemId != other.itemId) return false

        return true
    }

    override fun hashCode(): Int {
        return itemId.hashCode()
    }

    companion object {
        fun from(itemCreateVo: ItemCreateVo): Item {
            return Item(
                name = itemCreateVo.name,
                price = itemCreateVo.price,
                imageUrl = itemCreateVo.imageUrl,
                discountType = itemCreateVo.discountType,
                storeType = itemCreateVo.storeType,
                yearMonth = itemCreateVo.yearMonth,
            )
        }
    }
}