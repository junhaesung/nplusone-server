package com.haeseong.nplusone.domain.item.detail

import com.haeseong.nplusone.domain.config.ConfigService
import com.haeseong.nplusone.domain.item.*
import com.haeseong.nplusone.domain.item.name.ItemNameRepository
import com.haeseong.nplusone.domain.scrapping.ScrappingResultVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ItemDetailService {
    fun create(scrappingResultVo: ScrappingResultVo)
    fun getItemDetails(itemQueryVo: ItemQueryVo): Slice<ItemDetailVo>
    fun getItemDetailPage(itemQueryVo: ItemQueryVo): Page<ItemDetailVo>
}

@Service
@Transactional(readOnly = true)
class ItemDetailServiceImpl(
    private val itemDetailRepository: ItemDetailRepository,
    private val itemNameRepository: ItemNameRepository,
    private val configService: ConfigService,
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

    override fun getItemDetails(itemQueryVo: ItemQueryVo): Slice<ItemDetailVo> {
        val discountTypes = itemQueryVo.discountType?.run { listOf(this) }
            ?: listOf(DiscountType.ONE_PLUS_ONE, DiscountType.TWO_PLUS_ONE)
        val storeTypes = itemQueryVo.storeType?.run { listOf(this) }
            ?: StoreType.values().toList()
        val referenceDate = configService.getReferenceDate()
        return itemDetailRepository.findByNameContainsAndDiscountTypeInAndStoreTypeInAndReferenceDateAndItemDetailIdGreaterThan(
            name = itemQueryVo.name ?: "",
            discountTypes = discountTypes,
            storeTypes = storeTypes,
            referenceDate = referenceDate,
            offsetId = itemQueryVo.offsetId,
            pageable = PageRequest.of(0, itemQueryVo.pageSize, Sort.Direction.ASC, "itemDetailId"),
        ).map { ItemDetailVo.from(it) }
    }

    override fun getItemDetailPage(itemQueryVo: ItemQueryVo): Page<ItemDetailVo> {
        val discountTypes = itemQueryVo.discountType?.run { listOf(this) }
            ?: listOf(DiscountType.ONE_PLUS_ONE, DiscountType.TWO_PLUS_ONE)
        val storeTypes = itemQueryVo.storeType?.run { listOf(this) }
            ?: StoreType.values().toList()
        val referenceDate = configService.getReferenceDate()
        return itemDetailRepository.findByNameContainsAndDiscountTypeInAndStoreTypeInAndReferenceDate(
            discountTypes = discountTypes,
            storeTypes = storeTypes,
            name = itemQueryVo.name ?: "",
            referenceDate = referenceDate,
            pageable = PageRequest.of(0, itemQueryVo.pageSize, Sort.Direction.ASC, "itemDetailId")
        ).map { ItemDetailVo.from(it) }
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