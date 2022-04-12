package com.haeseong.nplusone.domain.config

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Config(
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val configId: Long = 0L,
    @Column(name = "configKey", unique = true)
    val key: String,
    @Column(name = "configValue")
    var value: String,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Config

        if (configId != other.configId) return false

        return true
    }

    override fun hashCode(): Int {
        return configId.hashCode()
    }

    companion object {
        fun of(key: String, value: String) = Config(
            key = key,
            value = value,
        )

        const val CONFIG_KEY_VALID_YEAR_MONTH = "VALID_YEAR_MONTH"
    }
}