package com.haeseong.nplusone.domain.search

import com.haeseong.nplusone.domain.member.Member
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class SearchHistory (
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val searchHistoryId: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "memberId")
    val member: Member,
    /**
     * 검색어
     */
    val searchWord: String,
    /**
     * 검색 시각
     */
    var searchedAt: LocalDateTime,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchHistory

        if (searchHistoryId != other.searchHistoryId) return false

        return true
    }

    override fun hashCode(): Int {
        return searchHistoryId.hashCode()
    }
}