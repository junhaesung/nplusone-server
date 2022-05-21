package com.haeseong.nplusone.domain.item

import com.haeseong.nplusone.domain.item.image.ItemImage
import com.haeseong.nplusone.domain.item.image.ItemImageRepository
import com.haeseong.nplusone.domain.item.name.ItemName
import com.haeseong.nplusone.domain.item.name.ItemNameRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ItemService {
    fun create(itemCreateVo: ItemCreateVo): ItemVo
}

@Transactional(readOnly = true)
@Service
class ItemServiceImpl(
    private val itemRepository: ItemRepository,
    private val itemNameRepository: ItemNameRepository,
    private val itemImageRepository: ItemImageRepository,
) : ItemService {

    @Transactional
    override fun create(itemCreateVo: ItemCreateVo): ItemVo {
        val item = getOrCreateItem(itemCreateVo = itemCreateVo)
        createItemName(itemCreateVo = itemCreateVo, item = item)
        createItemImage(itemCreateVo = itemCreateVo, item = item)
        return ItemVo.from(item = item)
    }

    private fun getOrCreateItem(itemCreateVo: ItemCreateVo): Item {
        val item = itemRepository.findByName(name = itemCreateVo.name)
        if (item != null) {
            return item
        }
        return itemRepository.save(
            Item(
                name = itemCreateVo.name,
                price = itemCreateVo.price,
            )
        )
    }

    private fun createItemName(itemCreateVo: ItemCreateVo, item: Item) {
        val existsByImageUrlAndStoreType = itemImageRepository.existsByImageUrlAndStoreType(
            imageUrl = itemCreateVo.imageUrl,
            storeType = itemCreateVo.storeType,
        )
        if (existsByImageUrlAndStoreType) {
            return
        }
        itemNameRepository.save(
            ItemName(
                item = item,
                name = itemCreateVo.name,
                storeType = itemCreateVo.storeType,
            )
        )
    }

    private fun createItemImage(itemCreateVo: ItemCreateVo, item: Item) {
        val existsByImageUrlAndStoreType = itemImageRepository.existsByImageUrlAndStoreType(
            imageUrl = itemCreateVo.imageUrl,
            storeType = itemCreateVo.storeType,
        )
        if (existsByImageUrlAndStoreType) {
            return
        }
        itemImageRepository.save(
            ItemImage(
                item = item,
                imageUrl = itemCreateVo.imageUrl,
                storeType = itemCreateVo.storeType,
            )
        )
    }

}