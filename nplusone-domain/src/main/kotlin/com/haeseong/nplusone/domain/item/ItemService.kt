package com.haeseong.nplusone.domain.item

import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
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
    private val scrappingResultService: ScrappingResultService,
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
        return scrappingResultService.getItems(itemQueryVo = itemQueryVo)
    }

}