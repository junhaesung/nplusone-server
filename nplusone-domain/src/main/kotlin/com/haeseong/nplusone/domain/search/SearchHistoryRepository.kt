package com.haeseong.nplusone.domain.search

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

@Suppress("FunctionName")
interface SearchHistoryRepository : JpaRepository<SearchHistory, Long> {
    fun findByMember_memberId(memberId: Long, pageable: Pageable): List<SearchHistory>
    fun findByMember_memberIdAndSearchHistoryId(memberId: Long, searchHistoryId: Long): SearchHistory?
}
