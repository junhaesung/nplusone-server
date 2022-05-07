package com.haeseong.nplusone.domain

import com.haeseong.nplusone.domain.item.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Transactional
@SpringBootTest
class ItemServiceImplTest {
    @Autowired
    lateinit var sut: ItemService

    @Autowired
    lateinit var itemRepository: ItemRepository

    @DisplayName("create: 성공")
    @Test
    fun create() {
        // given
        val itemCreateVo = ItemCreateVo(
            name = "name",
            price = BigDecimal.valueOf(5000),
            imageUrl = "imageUrl",
            discountType = DiscountType.ONE_PLUS_ONE,
            storeType = StoreType.CU,
            referenceDate = LocalDate.now(),
        )
        // when
        val actual = sut.create(itemCreateVo = itemCreateVo)
        // then
        assertEquals("name", actual.name)
        assertEquals(BigDecimal.valueOf(5000), actual.price)
        assertEquals("imageUrl", actual.imageUrl)
        assertEquals(DiscountType.ONE_PLUS_ONE, actual.discountType)
        assertEquals(StoreType.CU, actual.storeType)
        assertEquals(LocalDate.now(), actual.referenceDate)
    }

    @DisplayName("create: 동일한 아이템 존재하면 실패")
    @Test
    fun create_duplicated() {
        val referenceDate = LocalDate.of(2022, 5, 8)
        itemRepository.saveAndFlush(Item(
            name = "name",
            price = BigDecimal.valueOf(5000),
            imageUrl = null,
            discountType = DiscountType.ONE_PLUS_ONE,
            storeType = StoreType.CU,
            referenceDate = referenceDate,
        ))
        assertThrows(ItemDuplicatedException::class.java) {
            sut.create(itemCreateVo = ItemCreateVo(
                name = "name",
                price = BigDecimal.valueOf(5000),
                imageUrl = "imageUrl",
                discountType = DiscountType.ONE_PLUS_ONE,
                storeType = StoreType.CU,
                referenceDate = referenceDate,
            ))
        }
    }

    @DisplayName("create: name, storeType, discountType 같아도 날짜 다르면 성공")
    @Test
    fun create_another_day() {
        // given
        val referenceDate = LocalDate.of(2022, 5, 8)
        itemRepository.saveAndFlush(Item(
            name = "name",
            price = BigDecimal.valueOf(5000),
            imageUrl = null,
            discountType = DiscountType.ONE_PLUS_ONE,
            storeType = StoreType.CU,
            referenceDate = referenceDate,
        ))
        // when
        val actual = sut.create(itemCreateVo = ItemCreateVo(
            name = "name",
            price = BigDecimal.valueOf(5000),
            imageUrl = "imageUrl",
            discountType = DiscountType.ONE_PLUS_ONE,
            storeType = StoreType.CU,
            referenceDate = referenceDate.plusDays(1L),
        ))
        // then
        assertEquals("name", actual.name)
        assertEquals(StoreType.CU, actual.storeType)
        assertEquals(DiscountType.ONE_PLUS_ONE, actual.discountType)
        assertEquals(referenceDate.plusDays(1L), actual.referenceDate)
    }
}