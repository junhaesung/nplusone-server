package com.haeseong.nplusone.domain.search

data class SearchRequestVo(
    val searchWord: String,
    val offsetId: Long?,
    val pageSize: Int,
)
