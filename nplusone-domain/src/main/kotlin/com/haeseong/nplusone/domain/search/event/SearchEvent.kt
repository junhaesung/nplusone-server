package com.haeseong.nplusone.domain.search.event

import java.time.LocalDateTime

data class SearchEvent(
    val memberId: Long,
    val searchWord: String,
    val searchedAt: LocalDateTime,
)