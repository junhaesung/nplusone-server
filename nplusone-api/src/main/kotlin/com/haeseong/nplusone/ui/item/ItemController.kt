package com.haeseong.nplusone.ui.item

import com.haeseong.nplusone.domain.item.ItemQueryVo
import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.ui.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/items")
class ItemController(
    private val scrappingResultService: ScrappingResultService,
) {
    @GetMapping
    fun getItems(
        @ModelAttribute itemQueryRequest: ItemQueryRequest,
    ): ApiResponse<List<ItemResponse>> {
        val itemQueryVo = ItemQueryVo(
            name = itemQueryRequest.name,
            discountType = itemQueryRequest.discountType,
            storeType = itemQueryRequest.storeType,
            offsetId = itemQueryRequest.offsetId,
            pageSize = itemQueryRequest.pageSize,
        )
        val itemResponseSlice = scrappingResultService.getItems(itemQueryVo = itemQueryVo)
            .map {
                ItemResponse(
                    itemId = it.scrappingResultId,
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