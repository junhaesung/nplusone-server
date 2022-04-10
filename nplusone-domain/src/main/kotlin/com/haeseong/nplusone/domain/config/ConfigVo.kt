package com.haeseong.nplusone.domain.config

data class ConfigVo(
    val configId: Long,
    val key: String,
    val value: String,
) {
    constructor(config: Config) : this(
        configId = config.configId,
        key = config.key,
        value = config.value,
    )
}
