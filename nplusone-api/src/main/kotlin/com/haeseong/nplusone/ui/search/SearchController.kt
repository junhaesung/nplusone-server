package com.haeseong.nplusone.ui.search

import com.haeseong.nplusone.domain.search.SearchRequestVo
import com.haeseong.nplusone.domain.search.SearchService
import com.haeseong.nplusone.ui.ApiResponse
import com.haeseong.nplusone.ui.item.ItemDetailResponse
import com.haeseong.nplusone.ui.item.toDto
import com.haeseong.nplusone.ui.resolveMemberId
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/api/v1/search")
class SearchController(
    private val searchService: SearchService,
) {
    @PostMapping
    fun search(
        @ApiIgnore authentication: Authentication,
        @RequestBody searchRequest: SearchRequest,
    ): ApiResponse<List<ItemDetailResponse>> {
        val itemDetailResponseSlice = searchService.search(
            memberId = authentication.resolveMemberId(),
            searchRequestVo = SearchRequestVo(
                searchWord = searchRequest.searchWord,
                discountType = searchRequest.discountType,
                storeType = searchRequest.storeType,
                offsetId = searchRequest.offsetId ?: 0L,
                pageSize = searchRequest.pageSize,
            ),
        ).map { it.toDto() }
        return ApiResponse.success(
            slice = itemDetailResponseSlice,
        )
    }

    @PostMapping("/count")
    fun count(
        @ApiIgnore authentication: Authentication,
        @RequestBody searchRequest: SearchRequest,
    ): ApiResponse<Long> {
        return ApiResponse.success(
            data = searchService.count(
                searchRequestVo = SearchRequestVo(
                    searchWord = searchRequest.searchWord,
                    discountType = searchRequest.discountType,
                    storeType = searchRequest.storeType,
                    offsetId = searchRequest.offsetId ?: 0L,
                    pageSize = searchRequest.pageSize,
                ),
            ),
        )
    }
}