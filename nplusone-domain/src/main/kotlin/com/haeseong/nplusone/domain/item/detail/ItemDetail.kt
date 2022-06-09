package com.haeseong.nplusone.domain.item.detail

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.Item
import com.haeseong.nplusone.domain.item.StoreType
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

/**
 * scrapping result 의 최신 버전만 관리
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class ItemDetail (
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val itemDetailId: Long = 0L,
    /**
     * 기준 item 정보
     */
    @ManyToOne
    @JoinColumn(name = "itemId")
    val item: Item,
    /**
     * 이름
     */
    var name: String,
    /**
     * 가격
     */
    var price: BigDecimal,
    /**
     * 이미지
     */
    var imageUrl: String?,
    /**
     * 할인 타입
     */
    @Enumerated(EnumType.STRING)
    var discountType: DiscountType,
    /**
     * 편의점
     */
    @Enumerated(EnumType.STRING)
    var storeType: StoreType,
    /**
     * 기준 일자
     */
    var referenceDate: LocalDate,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemDetail

        if (itemDetailId != other.itemDetailId) return false

        return true
    }

    override fun hashCode(): Int {
        return itemDetailId.hashCode()
    }

}
