package com.haeseong.nplusone.domain.item

import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    fun findByName(name: String): Item?
}