package com.haeseong.nplusone.ui.item

import com.haeseong.nplusone.domain.item.ItemQueryVo
import com.haeseong.nplusone.domain.item.detail.ItemDetailService
import com.haeseong.nplusone.ui.ApiResponse
import org.springframework.web.bind.annotation.*

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
            .map { it.toDto() }
        return ApiResponse.success(itemResponseSlice)
    }

    @PostMapping("/count")
    fun countItems(
        @ModelAttribute itemQueryRequest: ItemQueryRequest,
    ): ApiResponse<Long> {
        val itemQueryVo = ItemQueryVo(
            name = itemQueryRequest.name,
            discountType = itemQueryRequest.discountType,
            storeType = itemQueryRequest.storeType,
            offsetId = itemQueryRequest.offsetId ?: 0,
            pageSize = itemQueryRequest.pageSize ?: 20,
        )
        return ApiResponse.success(
            data = itemDetailService.countItems(itemQueryVo = itemQueryVo)
        )
    }
}