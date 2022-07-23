package com.haeseong.nplusone.ui.recommend

import com.haeseong.nplusone.domain.recommend.RecommendService
import com.haeseong.nplusone.ui.ApiResponse
import com.haeseong.nplusone.ui.BadRequestException
import com.haeseong.nplusone.ui.item.ItemDetailResponse
import com.haeseong.nplusone.ui.item.toDto
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@RestController("/api/v1/recommend")
class RecommendController(
    private val recommendService: RecommendService,
) {
    @PostMapping
    fun recommend(
        @ApiIgnore authentication: Authentication,
        @RequestBody recommendRequest: RecommendRequest,
    ): ApiResponse<List<ItemDetailResponse>> = ApiResponse.success(
        slice = when {
            recommendRequest.storeType != null -> {
                recommendService.recommend(storeType = recommendRequest.storeType)
            }
            recommendRequest.discountType != null -> {
                recommendService.recommend(discountType = recommendRequest.discountType)
            }
            else -> {
                throw BadRequestException()
            }
        }.map { it.toDto() }
    )
}