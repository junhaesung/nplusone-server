package com.haeseong.nplusone.ui.member

import com.fasterxml.jackson.databind.ObjectMapper
import com.haeseong.nplusone.application.TokenService
import com.haeseong.nplusone.domain.member.IdProviderType
import com.haeseong.nplusone.infrastructure.firebase.FirebaseConfig
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var tokenService: TokenService<Long>

    @MockBean
    lateinit var firebaseConfig: FirebaseConfig

    @DisplayName("login 성공: FIREBASE")
    @Test
    fun login_firebase() {
        // given
        val loginRequestDto = LoginRequestDto(
            idProviderType = IdProviderType.FIREBASE,
            idProviderUserId = "idProviderUserId",
        )
        `when`(tokenService.encode(anyLong())).thenReturn("accessToken")
        // when
        mockMvc.perform(post("/api/v1/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(loginRequestDto)))
            // then
            .andExpect(status().isOk)
    }

    @DisplayName("login 성공: UUID")
    @Test
    fun login_uuid() {
        // given
        val loginRequestDto = LoginRequestDto(
            idProviderType = IdProviderType.UUID,
            idProviderUserId = UUID.randomUUID().toString(),
        )
        `when`(tokenService.encode(anyLong())).thenReturn("accessToken")
        // when
        mockMvc.perform(post("/api/v1/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(loginRequestDto)))
            // then
            .andExpect(status().isOk)
    }
}
