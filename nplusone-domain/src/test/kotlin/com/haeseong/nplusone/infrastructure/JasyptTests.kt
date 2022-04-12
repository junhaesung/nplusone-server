package com.haeseong.nplusone.infrastructure

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.ConfigurableEnvironment

@Disabled
@SpringBootTest
class JasyptTests(
    @Value("\${jasypt.encryptor.password}")
    val password: String?,
    @Autowired
    val configurableEnvironment: ConfigurableEnvironment,
) {
    lateinit var encryptor: DefaultLazyEncryptor

    @BeforeEach
    internal fun setUp() {
        if (password.isNullOrBlank()) {
            throw IllegalStateException("'password' must not be null or blank")
        }
        encryptor = DefaultLazyEncryptor(configurableEnvironment)
    }

    @Test
    fun encrypt() {
        val input = ""
        val actual = encryptor.encrypt(input)
        println("---------------------------------")
        println("encrypted: ENC($actual)")
        println("---------------------------------")
    }
}