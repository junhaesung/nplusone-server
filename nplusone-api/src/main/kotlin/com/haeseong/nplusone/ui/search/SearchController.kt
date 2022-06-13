package com.haeseong.nplusone.ui.search

import com.haeseong.nplusone.domain.item.detail.ItemDetailVo
import com.haeseong.nplusone.domain.search.SearchRequestVo
import com.haeseong.nplusone.domain.search.SearchService
import com.haeseong.nplusone.ui.ApiResponse
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
    ): ApiResponse<List<ItemDetailVo>> {
        val itemDetailVoPage = searchService.search(
            memberId = authentication.resolveMemberId(),
            searchRequestVo = SearchRequestVo(
                searchWord = searchRequest.searchWord,
                offsetId = searchRequest.offsetId,
                pageSize = searchRequest.pageSize,
            ),
        )
        return ApiResponse.success(
            page = itemDetailVoPage,
        )
    }
}