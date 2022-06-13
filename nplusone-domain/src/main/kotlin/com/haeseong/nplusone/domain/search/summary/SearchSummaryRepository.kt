package com.haeseong.nplusone.domain.search.summary

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface SearchSummaryRepository : JpaRepository<SearchSummary, Long> {
    fun findByReferenceDateTimeOrderBySearchRankDesc(referenceDateTime: LocalDateTime): List<SearchSummary>
}