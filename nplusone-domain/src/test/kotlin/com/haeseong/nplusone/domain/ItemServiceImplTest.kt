package com.haeseong.nplusone.domain

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.ItemCreateVo
import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.StoreType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.YearMonth

@Transactional
@SpringBootTest
class ItemServiceImplTest {
    @Autowired
    lateinit var sut: ItemService

    @Test
    fun create() {
        // given
        val itemCreateVo = ItemCreateVo(
            name = "name",
            price = BigDecimal.valueOf(5000),
            imageUrl = "imageUrl",
            discountType = DiscountType.ONE_PLUS_ONE,
            storeType = StoreType.CU,
            yearMonth = YearMonth.now(),
        )
        // when
        val actual = sut.create(itemCreateVo = itemCreateVo)
        // then
        assertEquals("name", actual.name)
        assertEquals(BigDecimal.valueOf(5000), actual.price)
        assertEquals("imageUrl", actual.imageUrl)
        assertEquals(DiscountType.ONE_PLUS_ONE, actual.discountType)
        assertEquals(StoreType.CU, actual.storeType)
        assertEquals(YearMonth.now(), actual.yearMonth)
    }
}