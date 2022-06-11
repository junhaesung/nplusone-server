package com.haeseong.nplusone.domain.scrapping

import com.haeseong.nplusone.domain.config.ConfigService
import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.ItemQueryVo
import com.haeseong.nplusone.domain.item.StoreType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

interface ScrappingResultService {
    fun create(scrappingResultCreateVo: ScrappingResultCreateVo): ScrappingResultVo
    fun getItems(itemQueryVo: ItemQueryVo): Slice<ScrappingResultVo>
    fun getScrappingResults(
        referenceDate: LocalDate,
        offsetScrappingResultId: Long,
        limit: Int,
    ): Slice<ScrappingResultVo>
}

@Service
@Transactional(readOnly = true)
class ScrappingResultServiceImpl(
    private val scrappingResultRepository: ScrappingResultRepository,
    private val configService: ConfigService,
) : ScrappingResultService {
    @Transactional
    override fun create(scrappingResultCreateVo: ScrappingResultCreateVo): ScrappingResultVo {
        val isDuplicated = scrappingResultRepository.existsByReferenceDateAndNameAndStoreTypeAndDiscountType(
            referenceDate = scrappingResultCreateVo.referenceDate,
            name = scrappingResultCreateVo.name,
            storeType = scrappingResultCreateVo.storeType,
            discountType = scrappingResultCreateVo.discountType,
        )
        if (isDuplicated) {
            throw ScrappingResultDuplicatedException()
        }
        return scrappingResultRepository.save(ScrappingResult.from(scrappingResultCreateVo))
            .let { ScrappingResultVo.from(it) }
    }

    override fun getItems(itemQueryVo: ItemQueryVo): Slice<ScrappingResultVo> {
        val discountTypes = itemQueryVo.discountType?.run { listOf(this) }
            ?: listOf(DiscountType.ONE_PLUS_ONE, DiscountType.TWO_PLUS_ONE)
        val storeTypes = itemQueryVo.storeType?.run { listOf(this) }
            ?: StoreType.values().toList()
        val validDate = configService.getReferenceDate()
        return scrappingResultRepository.findByNameContainsAndDiscountTypeInAndStoreTypeInAndReferenceDateAndScrappingResultIdGreaterThan(
            name = itemQueryVo.name ?: "",
            discountTypes = discountTypes,
            storeTypes = storeTypes,
            referenceDate = validDate,
            offsetId = itemQueryVo.offsetId
        ).map { ScrappingResultVo.from(it) }
    }

    override fun getScrappingResults(
        referenceDate: LocalDate,
        offsetScrappingResultId: Long,
        limit: Int,
    ): Slice<ScrappingResultVo> {
        return scrappingResultRepository.findByReferenceDateAndScrappingResultIdGreaterThanOrderByScrappingResultId(
            referenceDate,
            offsetScrappingResultId,
            PageRequest.of(0, limit, Sort.Direction.ASC, "scrappingResultId")
        ).map { ScrappingResultVo.from(it) }
    }
}