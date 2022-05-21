package com.haeseong.nplusone.ui.item

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.domain.scrapping.ScrappingResultCreateVo
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.ui.ApiResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class ItemControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var itemService: ItemService

    @Autowired
    lateinit var scrappingResultService: ScrappingResultService

    val objectMapper: ObjectMapper = jsonMapper {
        addModule(kotlinModule())
        addModule(JavaTimeModule())
    }

    @DisplayName("getItems: 결과가 없는 경우")
    @Test
    fun getItems_empty() {
        // given
        // when
        val andReturn = mockMvc.perform(get("/api/v1/items"))
            .andExpect(status().isOk)
            .andReturn()
        val apiResponse: ApiResponse<List<ItemResponse>> = objectMapper.readValue(andReturn.response.contentAsByteArray)
        // then
        assertNotNull(apiResponse.data)
        assertTrue(apiResponse.data!!.isEmpty())
    }

    @DisplayName("getItems: 이름으로 조회 성공")
    @Test
    fun getItems_queryByName() {
        // given
        val name = "name"
        scrappingResultService.create(ScrappingResultCreateVo(
            name = name,
            price = BigDecimal.valueOf(10000),
            imageUrl = "imageUrl",
            discountType = DiscountType.ONE_PLUS_ONE,
            storeType = StoreType.GS25,
            referenceDate = LocalDate.now(),
        ))
        scrappingResultService.create(ScrappingResultCreateVo(
            name = "anotherName",
            price = BigDecimal.valueOf(20000),
            imageUrl = "imageUrl",
            discountType = DiscountType.ONE_PLUS_ONE,
            storeType = StoreType.GS25,
            referenceDate = LocalDate.now(),
        ))
        // when
        val andReturn = mockMvc.perform(get("/api/v1/items")
            .param("name", name))
            .andExpect(status().isOk)
            .andReturn()
        val apiResponse: ApiResponse<List<ItemResponse>> = objectMapper.readValue(andReturn.response.contentAsByteArray)
        // then
        assertNotNull(apiResponse.data)
        assertEquals(1, apiResponse.data!!.size)
        assertEquals("name", apiResponse.data!!.first().name)
    }
}