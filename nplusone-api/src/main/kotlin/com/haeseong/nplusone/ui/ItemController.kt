package com.haeseong.nplusone.ui

import com.haeseong.nplusone.domain.DiscountType
import com.haeseong.nplusone.domain.ItemQueryVo
import com.haeseong.nplusone.domain.ItemService
import com.haeseong.nplusone.domain.StoreType
import org.springframework.data.domain.Slice
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
                )
            }
        return ApiResponse.success(itemResponseSlice)
    }
}