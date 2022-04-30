package com.haeseong.nplusone.domain.config

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

interface ConfigService {
    fun create(key: String, value: String): ConfigVo
    fun get(key: String): ConfigVo?
    fun setReferenceDate(localDate: LocalDate)
    fun getReferenceDate(): LocalDate
}

@Transactional(readOnly = true)
@Service
class ConfigServiceImpl(
    private val configRepository: ConfigRepository,
) : ConfigService {
    @Transactional
    override fun create(key: String, value: String): ConfigVo {
        val config = Config.of(key, value)
        return configRepository.save(config)
            .run { ConfigVo(this) }
    }

    override fun get(key: String): ConfigVo? {
        return configRepository.findByKey(key)
            ?.run { ConfigVo(this) }
    }

    @Transactional
    override fun setReferenceDate(localDate: LocalDate) {
        val validDateString = localDate.toString()
        configRepository.findByKey(Config.CONFIG_KEY_REFERENCE_DATE)
            ?.let { it.value = validDateString }
            ?: configRepository.save(Config.of(
                key = Config.CONFIG_KEY_REFERENCE_DATE,
                value = validDateString,
            ))
    }

    override fun getReferenceDate(): LocalDate {
        return get(Config.CONFIG_KEY_REFERENCE_DATE)
            ?.let { LocalDate.parse(it.value) }
            ?: LocalDate.now()
    }
}