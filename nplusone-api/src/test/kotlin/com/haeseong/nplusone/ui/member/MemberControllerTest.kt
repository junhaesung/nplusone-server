package com.haeseong.nplusone.ui.member

import com.fasterxml.jackson.databind.ObjectMapper
import com.haeseong.nplusone.application.TokenService
import com.haeseong.nplusone.domain.item.member.IdProviderType
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

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var tokenService: TokenService<Long>

    @Test
    fun login() {
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
}