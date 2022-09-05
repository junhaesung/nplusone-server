package com.haeseong.nplusone

import com.haeseong.nplusone.infrastructure.firebase.FirebaseConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean

@TestConfiguration
@SpringBootApplication
class TestConfig {
    @MockBean
    lateinit var firebaseConfig: FirebaseConfig
}