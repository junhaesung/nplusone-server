package com.haeseong.nplusone.domain.config

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

interface ConfigService {
    fun create(key: String, value: String): ConfigVo
    fun get(key: String): ConfigVo?
    fun setValidDate(localDate: LocalDate)
    fun getValidDate(): LocalDate
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
    override fun setValidDate(localDate: LocalDate) {
        val validDateString = localDate.toString()
        configRepository.findByKey(Config.CONFIG_KEY_VALID_DATE)
            ?.let { it.value = validDateString }
            ?: configRepository.save(Config.of(
                key = Config.CONFIG_KEY_VALID_DATE,
                value = validDateString,
            ))
    }

    override fun getValidDate(): LocalDate {
        return get(Config.CONFIG_KEY_VALID_DATE)
            ?.let { LocalDate.parse(it.value) }
            ?: LocalDate.now()
    }
}