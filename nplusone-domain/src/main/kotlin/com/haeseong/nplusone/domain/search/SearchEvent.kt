package com.haeseong.nplusone.domain.search

import java.time.LocalDateTime

data class SearchEvent(
    val memberId: Long,
    val searchWord: String,
    val searchedAt: LocalDateTime,
)