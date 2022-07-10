package com.haeseong.nplusone.ui.item

import com.haeseong.nplusone.domain.item.ItemQueryVo
import com.haeseong.nplusone.domain.item.detail.ItemDetailService
import com.haeseong.nplusone.ui.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/items")
class ItemController(
    private val itemDetailService: ItemDetailService,
) {
    @GetMapping
    fun getItems(
        @ModelAttribute itemQueryRequest: ItemQueryRequest,
    ): ApiResponse<List<ItemDetailResponse>> {
        val itemQueryVo = ItemQueryVo(
            name = itemQueryRequest.name,
            discountType = itemQueryRequest.discountType,
            storeType = itemQueryRequest.storeType,
            offsetId = itemQueryRequest.offsetId ?: 0,
            pageSize = itemQueryRequest.pageSize ?: 20,
        )
        val itemResponseSlice = itemDetailService.getItemDetails(itemQueryVo = itemQueryVo)
            .map {
                ItemDetailResponse(
                    itemDetailId = it.itemDetailId,
                    itemId = it.itemId,
                    name = it.name,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    discountType = it.discountType,
                    storeType = it.storeType,
                    referenceDate = it.referenceDate,
                )
            }
        return ApiResponse.success(itemResponseSlice)
    }
}