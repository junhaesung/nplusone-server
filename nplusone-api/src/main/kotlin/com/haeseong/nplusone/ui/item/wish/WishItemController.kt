package com.haeseong.nplusone.ui.item.wish

import com.haeseong.nplusone.domain.item.wish.WishItemService
import com.haeseong.nplusone.ui.ApiResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/api/v1/wish-items")
class WishItemController(
    private val wishItemService: WishItemService,
) {
    @PostMapping
    fun add(
        @ApiIgnore authentication: Authentication,
        @RequestBody wishItemCreateRequest: WishItemCreateRequest,
    ): ApiResponse<*> {
        wishItemService.add(
            memberId = resolveMemberId(authentication),
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
            memberId = resolveMemberId(authentication),
            itemId = itemId,
        )
        return ApiResponse.success()
    }

    private fun resolveMemberId(authentication: Authentication): Long {
        if (authentication is PreAuthenticatedAuthenticationToken) {
            return authentication.name.toLong()
        }
        throw IllegalArgumentException()
    }
}