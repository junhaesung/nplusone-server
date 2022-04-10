package com.haeseong.nplusone.ui.item

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.ItemQueryVo
import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.ui.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/items")
class ItemController(
    private val itemService: ItemService,
) {
    @GetMapping
    fun list(
        @RequestParam(required = false) discountType: DiscountType?,
        @RequestParam(required = false) storeType: StoreType?,
        @RequestParam(required = false, defaultValue = "0") offsetId: Long,
        @RequestParam(required = false, defaultValue = "20") pageSize: Int,
    ): ApiResponse<List<ItemResponse>> {
        val itemQueryVo = ItemQueryVo(
            discountType = discountType,
            storeType = storeType,
            offsetId = offsetId,
            pageSize = pageSize,
        )
        val itemResponseSlice = itemService.getItems(itemQueryVo = itemQueryVo)
            .map {
                ItemResponse(
                    itemId = it.itemId,
                    name = it.name,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    discountType = it.discountType,
                    storeType = it.storeType,
                    yearMonth = it.yearMonth,
                )
            }
        return ApiResponse.success(itemResponseSlice)
    }
}