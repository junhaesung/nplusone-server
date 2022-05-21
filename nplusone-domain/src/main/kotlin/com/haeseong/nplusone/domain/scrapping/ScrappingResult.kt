package com.haeseong.nplusone.domain.scrapping

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class ScrappingResult(
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val scrappingResultId: Long = 0L,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    @Enumerated(EnumType.STRING)
    val discountType: DiscountType,
    @Enumerated(EnumType.STRING)
    val storeType: StoreType,
    val referenceDate: LocalDate,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScrappingResult

        if (scrappingResultId != other.scrappingResultId) return false

        return true
    }

    override fun hashCode(): Int {
        return scrappingResultId.hashCode()
    }

    companion object {
        fun from(scrappingResultCreateVo: ScrappingResultCreateVo) = ScrappingResult(
            name = scrappingResultCreateVo.name,
            price = scrappingResultCreateVo.price,
            imageUrl = scrappingResultCreateVo.imageUrl,
            discountType = scrappingResultCreateVo.discountType,
            storeType = scrappingResultCreateVo.storeType,
            referenceDate = scrappingResultCreateVo.referenceDate,
        )
    }
}