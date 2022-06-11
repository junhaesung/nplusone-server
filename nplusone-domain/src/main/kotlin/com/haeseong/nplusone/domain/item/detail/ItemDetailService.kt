package com.haeseong.nplusone.domain.item.detail

import com.haeseong.nplusone.domain.item.Item
import com.haeseong.nplusone.domain.item.ItemNotFoundException
import com.haeseong.nplusone.domain.item.name.ItemNameRepository
import com.haeseong.nplusone.domain.scrapping.ScrappingResultVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ItemDetailService {
    fun create(scrappingResultVo: ScrappingResultVo)
}

@Service
@Transactional(readOnly = true)
class ItemDetailServiceImpl(
    private val itemDetailRepository: ItemDetailRepository,
    private val itemNameRepository: ItemNameRepository,
) : ItemDetailService {
    @Transactional
    override fun create(scrappingResultVo: ScrappingResultVo) {
        val alreadyExists = itemDetailRepository.existsByNameAndDiscountTypeAndStoreTypeAndReferenceDate(
            name = scrappingResultVo.name,
            discountType = scrappingResultVo.discountType,
            storeType = scrappingResultVo.storeType,
            referenceDate = scrappingResultVo.referenceDate,
        )
        if (alreadyExists) {
            throw ItemDetailDuplicatedException()
        }
        itemDetailRepository.save(
            ItemDetail(
                item = resolveItem(scrappingResultVo),
                name = scrappingResultVo.name,
                price = scrappingResultVo.price,
                imageUrl = scrappingResultVo.imageUrl,
                discountType = scrappingResultVo.discountType,
                storeType = scrappingResultVo.storeType,
                referenceDate = scrappingResultVo.referenceDate,
            )
        )
    }

    private fun resolveItem(scrappingResultVo: ScrappingResultVo): Item {
        try {
            return itemNameRepository.findByNameAndStoreType(
                name = scrappingResultVo.name,
                storeType = scrappingResultVo.storeType,
            )?.item ?: throw ItemNotFoundException()
        } catch (e: IncorrectResultSizeDataAccessException) {
            log.error("Failed to resolve item. name:${scrappingResultVo.name}", e)
            throw e
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ItemDetailServiceImpl::class.java)
    }
}