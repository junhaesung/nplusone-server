package com.haeseong.nplusone.ui.search

data class SearchRequest(
    val searchWord: String,
    val offsetId: Long,
    val pageSize: Int,
)
