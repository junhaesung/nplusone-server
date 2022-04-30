package com.haeseong.nplusone.domain.config

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import java.time.YearMonth

interface ConfigService {
    fun create(key: String, value: String): ConfigVo
    fun get(key: String): ConfigVo?
    fun setValidYearMonth(yearMonth: YearMonth)
    fun getValidYearMonth(): YearMonth
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
    override fun setValidYearMonth(yearMonth: YearMonth) {
        val yearMonthString = yearMonth.toString()
        configRepository.findByKey(Config.CONFIG_KEY_VALID_YEAR_MONTH)
            ?.let { it.value = yearMonthString }
            ?: configRepository.save(Config.of(
                key = Config.CONFIG_KEY_VALID_YEAR_MONTH,
                value = yearMonthString,
            ))
    }

    override fun getValidYearMonth(): YearMonth {
        return get(Config.CONFIG_KEY_VALID_YEAR_MONTH)
            ?.let { YearMonth.parse(it.value) }
            ?: YearMonth.now()
    }
}