package com.haeseong.nplusone.domain

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
) : ItemService {

    @Transactional
    override fun create(itemCreateVo: ItemCreateVo): ItemVo {
        val item = Item.from(itemCreateVo = itemCreateVo)
        return itemRepository.save(item)
            .run { ItemVo.from(this) }
    }

    override fun getItems(itemQueryVo: ItemQueryVo): Slice<ItemVo> {
        val discountTypes = itemQueryVo.discountType?.run { listOf(this) }
            ?: listOf(DiscountType.ONE_PLUS_ONE, DiscountType.TWO_PLUS_ONE)
        val storeTypes = itemQueryVo.storeType?.run { listOf(this) }
            ?: StoreType.values().toList()
        return itemRepository.findByDiscountTypeInAndStoreTypeInAndItemIdGreaterThan(
            discountTypes = discountTypes,
            storeTypes = storeTypes,
            offsetId = itemQueryVo.offsetId
        ).map { ItemVo.from(it) }
    }

}