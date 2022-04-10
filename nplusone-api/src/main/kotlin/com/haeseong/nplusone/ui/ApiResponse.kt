package com.haeseong.nplusone.ui

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.domain.Page
import org.springframework.data.domain.Slice

data class ApiResponse<T>(
    val data: T?,
    val code: String,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val pageSize: Int? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val pageNumber: Int? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val totalCount: Long? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val hasNext: Boolean? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val offsetId: Long? = null,
) {
    companion object {
        fun success() = ApiResponse(
            data = null,
            code = "SUCCESS",
            message = "성공",
        )

        fun <T> success(data: T) = ApiResponse(
            data = data,
            code = "SUCCESS",
            message = "성공",
        )

        fun <T> success(page: Page<T>) = ApiResponse(
            data = page.content,
            code = "SUCCESS",
            message = "성공",
            pageSize = page.size,
            pageNumber = page.number,
            totalCount = page.totalElements,
            hasNext = page.hasNext(),
        )

        fun <T : OffsetIdSupport> success(slice: Slice<T>) = ApiResponse(
            data = slice.content,
            code = "SUCCESS",
            message = "성공",
            hasNext = slice.hasNext(),
            offsetId = if (!slice.hasContent()) null else slice.content.last().offsetId,
        )

        fun <T> success(collection: Collection<T>) = ApiResponse(
            data = collection,
            code = "SUCCESS",
            message = "성공",
            totalCount = collection.size.toLong(),
            hasNext = false,
        )

        fun failure() = ApiResponse(
            data = null,
            code = "FAILURE",
            message = "실패",
        )
    }
}