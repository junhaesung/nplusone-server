package com.haeseong.nplusone.domain.config

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ConfigService {
    fun create(key: String, value: String): ConfigVo
    fun get(key: String): ConfigVo?
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

}