package com.haeseong.nplusone.domain.search.history

import com.haeseong.nplusone.domain.search.summary.SearchWordRanking
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

@Suppress("FunctionName")
interface SearchHistoryRepository : JpaRepository<SearchHistory, Long> {
    fun findByMember_memberId(memberId: Long, pageable: Pageable): List<SearchHistory>
    fun findByMember_memberIdAndSearchHistoryId(memberId: Long, searchHistoryId: Long): SearchHistory?
    @Query(
        value = "SELECT search_word as searchWord, count(search_history_id) AS searchCount " +
                "FROM search_history " +
                "WHERE searched_at >= ?1 " +
                "AND searched_at < ?2 " +
                "GROUP BY search_word " +
                "ORDER BY count(search_history_id) desc " +
                "LIMIT ?3",
        nativeQuery = true,
    )
    fun associateBySearchWord(begin: LocalDateTime, end: LocalDateTime, limit: Int): List<SearchWordRanking>
}
