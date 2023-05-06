package com.haeseong.nplusone.infrastructure.hibernate

import org.springframework.batch.item.database.AbstractPagingItemReader
import java.util.concurrent.CopyOnWriteArrayList

class JpaRepositoryPagingItemReader<T>(
    private val query: (page: Int, size: Int) -> List<T>,
) : AbstractPagingItemReader<T>() {

    override fun doReadPage() {
        if (results == null) {
            results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }
        results.addAll(query(page, pageSize))
    }

    override fun doJumpToPage(itemIndex: Int) {
        // Do nothing
    }
}
