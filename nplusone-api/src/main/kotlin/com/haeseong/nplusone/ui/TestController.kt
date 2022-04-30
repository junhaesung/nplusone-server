package com.haeseong.nplusone.ui

import com.haeseong.nplusone.domain.item.ItemCreateVo
import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.ItemVo
import com.haeseong.nplusone.ui.item.ItemCreateRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.YearMonth

@RestController
@RequestMapping("/api/test/items")
class TestController(
    private val itemService: ItemService,
) {
    @PostMapping
    fun create(
        @RequestBody itemCreateRequest: ItemCreateRequest,
    ): ItemVo {
        val itemCreateVo = ItemCreateVo(
            name = itemCreateRequest.name,
            price = itemCreateRequest.price,
            imageUrl = itemCreateRequest.imageUrl,
            discountType = itemCreateRequest.discountType,
            storeType = itemCreateRequest.storeType,
            referenceDate = LocalDate.now(),
        )
        return itemService.create(itemCreateVo = itemCreateVo)
    }
}