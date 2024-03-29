package com.haeseong.nplusone.domain.search.event

import com.haeseong.nplusone.domain.search.history.SearchHistoryService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

interface SearchEventSubscriber {
    @TransactionalEventListener(SearchEvent::class)
    fun subscribeAsync(searchEvent: SearchEvent)
}

@Component
class SearchEventApplicationEventSubscriber(
    private val searchHistoryService: SearchHistoryService,
) : SearchEventSubscriber {

    @Async
    override fun subscribeAsync(searchEvent: SearchEvent) {
        record(searchEvent = searchEvent)
    }

    private fun record(searchEvent: SearchEvent) {
        searchHistoryService.record(searchEvent = searchEvent)
    }
}