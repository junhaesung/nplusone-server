package com.haeseong.nplusone.domain.config

import org.springframework.data.jpa.repository.JpaRepository

interface ConfigRepository : JpaRepository<Config, Long> {
    fun findByKey(key: String): Config?
}