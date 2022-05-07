package com.haeseong.nplusone.domain.item

import com.haeseong.nplusone.domain.config.ConfigService
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ItemService {
    fun create(itemCreateVo: ItemCreateVo): ItemVo
    fun getItems(itemQueryVo: ItemQueryVo): Slice<ItemVo>
}

@Transactional(readOnly = true)
@Service
class ItemServiceImpl(
    private val itemRepository: ItemRepository,
    private val configService: ConfigService,
) : ItemService {

    @Transactional
    override fun create(itemCreateVo: ItemCreateVo): ItemVo {
        val item = Item.from(itemCreateVo = itemCreateVo)
        if (itemRepository.existsByReferenceDateAndNameAndStoreTypeAndDiscountType(
                referenceDate = itemCreateVo.referenceDate,
                name = itemCreateVo.name,
                storeType = itemCreateVo.storeType,
                discountType = itemCreateVo.discountType,
            )
        ) {
            throw ItemDuplicatedException()
        }
        return itemRepository.save(item)
            .run { ItemVo.from(this) }
    }

    override fun getItems(itemQueryVo: ItemQueryVo): Slice<ItemVo> {
        val discountTypes = itemQueryVo.discountType?.run { listOf(this) }
            ?: listOf(DiscountType.ONE_PLUS_ONE, DiscountType.TWO_PLUS_ONE)
        val storeTypes = itemQueryVo.storeType?.run { listOf(this) }
            ?: StoreType.values().toList()
        val validDate = configService.getReferenceDate()
        return itemRepository.findByNameContainsAndDiscountTypeInAndStoreTypeInAndReferenceDateAndItemIdGreaterThan(
            name = itemQueryVo.name ?: "",
            discountTypes = discountTypes,
            storeTypes = storeTypes,
            referenceDate = validDate,
            offsetId = itemQueryVo.offsetId
        ).map { ItemVo.from(it) }
    }

}