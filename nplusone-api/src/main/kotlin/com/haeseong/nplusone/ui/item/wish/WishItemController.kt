package com.haeseong.nplusone.ui.item.wish

import com.haeseong.nplusone.domain.item.wish.WishItemService
import com.haeseong.nplusone.ui.ApiResponse
import com.haeseong.nplusone.ui.item.toDto
import com.haeseong.nplusone.ui.resolveMemberId
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/api/v1/wish-items")
class WishItemController(
    private val wishItemService: WishItemService,
) {
    @GetMapping
    fun getWishItems(
        @ApiIgnore authentication: Authentication,
    ): ApiResponse<*> {
        return ApiResponse.success(
            data = wishItemService.getWishItems(
                memberId = authentication.resolveMemberId()
            ).map { it.toDto() }
        )
    }

    @PostMapping
    fun add(
        @ApiIgnore authentication: Authentication,
        @RequestBody wishItemCreateRequest: WishItemCreateRequest,
    ): ApiResponse<*> {
        wishItemService.add(
            memberId = authentication.resolveMemberId(),
            itemId = wishItemCreateRequest.itemId
        )
        return ApiResponse.success()
    }

    @DeleteMapping("/{itemId}")
    fun remove(
        @ApiIgnore authentication: Authentication,
        @PathVariable itemId: Long,
    ): ApiResponse<*> {
        wishItemService.remove(
            memberId = authentication.resolveMemberId(),
            itemId = itemId,
        )
        return ApiResponse.success()
    }
}