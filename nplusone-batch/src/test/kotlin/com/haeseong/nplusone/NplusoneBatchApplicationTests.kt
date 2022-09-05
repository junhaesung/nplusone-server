package com.haeseong.nplusone

import com.haeseong.nplusone.infrastructure.firebase.FirebaseConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class NplusoneBatchApplicationTests {
	@MockBean
	lateinit var firebaseConfig: FirebaseConfig

	@Test
	fun contextLoads() {
	}

}
