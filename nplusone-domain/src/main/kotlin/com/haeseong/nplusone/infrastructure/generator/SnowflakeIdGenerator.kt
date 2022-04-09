package com.haeseong.nplusone.infrastructure.generator

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.ThreadLocalRandom

class SnowflakeIdGenerator : IdentifierGenerator {
    @Throws(HibernateException::class)
    override fun generate(session: SharedSessionContractImplementor, `object`: Any): Serializable {
        val currentSeconds = LocalDateTime.now().toInstant(ZONE_OFFSET_KST).toEpochMilli()
        val random = ThreadLocalRandom.current().nextLong((1 shl 23).toLong())
        return (currentSeconds - START_SECONDS shl 23) + random
    }

    companion object {
        private val ZONE_OFFSET_KST = ZoneOffset.of("+09:00")
        private val START_SECONDS = LocalDate.of(2022, 4, 1)
                .atStartOfDay()
                .toInstant(ZONE_OFFSET_KST)
                .toEpochMilli()
    }
}
