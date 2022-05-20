package com.haeseong.nplusone.domain.scrapping

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ScrappingResultService {
    fun create(scrappingResultCreateVo: ScrappingResultCreateVo): ScrappingResultVo
}

@Service
class ScrappingResultServiceImpl(
    private val scrappingResultRepository: ScrappingResultRepository,
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
}