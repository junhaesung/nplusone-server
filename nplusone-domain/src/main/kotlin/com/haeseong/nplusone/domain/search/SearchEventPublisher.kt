package com.haeseong.nplusone.domain.search

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.time.LocalDateTime

interface SearchEventPublisher {
    fun publish(memberId: Long, searchWord: String, searchedAt: LocalDateTime)
}

@Component
class SearchEventApplicationEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : SearchEventPublisher {
    override fun publish(memberId: Long, searchWord: String, searchedAt: LocalDateTime) {
        applicationEventPublisher.publishEvent(
            SearchEvent(
                memberId = memberId,
                searchWord = searchWord,
                searchedAt = searchedAt,
            )
        )
    }
}
