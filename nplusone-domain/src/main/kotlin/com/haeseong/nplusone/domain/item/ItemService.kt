package com.haeseong.nplusone.domain.item

import com.haeseong.nplusone.domain.item.image.ItemImage
import com.haeseong.nplusone.domain.item.image.ItemImageRepository
import com.haeseong.nplusone.domain.item.name.ItemName
import com.haeseong.nplusone.domain.item.name.ItemNameRepository
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import org.springframework.data.domain.Slice
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
        val createdItem = createItem(itemCreateVo = itemCreateVo)
        createItemName(itemCreateVo = itemCreateVo, createdItem = createdItem)
        createItemImage(itemCreateVo = itemCreateVo, createdItem = createdItem)
        return ItemVo.from(item = createdItem)
    }

    private fun createItem(itemCreateVo: ItemCreateVo): Item {
        if (itemRepository.existsByName(name = itemCreateVo.name)) {
            throw ItemDuplicatedException(message = "'name' is already used. itemCreateVo:$itemCreateVo")
        }
        return itemRepository.save(
            Item(
                name = itemCreateVo.name,
                price = itemCreateVo.price,
            )
        )
    }

    private fun createItemName(itemCreateVo: ItemCreateVo, createdItem: Item) {
        val existsByImageUrlAndStoreType = itemImageRepository.existsByImageUrlAndStoreType(
            imageUrl = itemCreateVo.imageUrl,
            storeType = itemCreateVo.storeType,
        )
        if (existsByImageUrlAndStoreType) {
            return
        }
        itemNameRepository.save(
            ItemName(
                item = createdItem,
                name = itemCreateVo.name,
                storeType = itemCreateVo.storeType,
            )
        )
    }

    private fun createItemImage(itemCreateVo: ItemCreateVo, createdItem: Item) {
        val existsByImageUrlAndStoreType = itemImageRepository.existsByImageUrlAndStoreType(
            imageUrl = itemCreateVo.imageUrl,
            storeType = itemCreateVo.storeType,
        )
        if (existsByImageUrlAndStoreType) {
            return
        }
        itemImageRepository.save(
            ItemImage(
                item = createdItem,
                imageUrl = itemCreateVo.imageUrl,
                storeType = itemCreateVo.storeType,
            )
        )
    }

}