package com.haeseong.nplusone.domain.search.summary

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@EntityListeners(AuditingEntityListener::class)
class SearchSummary(
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val searchSummaryId: Long = 0L,
    /**
     * 검색어
     */
    val searchWord: String,
    /**
     * 기준 시각 (yyyy-MM-dd HH:00:00)
     */
    val referenceDateTime: LocalDateTime,
    /**
     * 개수
     */
    val searchCount: Int,
    /**
     * 순위
     */
    val searchRank: Int,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchSummary

        if (searchSummaryId != other.searchSummaryId) return false

        return true
    }

    override fun hashCode(): Int {
        return searchSummaryId.hashCode()
    }
}