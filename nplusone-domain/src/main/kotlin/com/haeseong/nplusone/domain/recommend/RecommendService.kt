package com.haeseong.nplusone.domain.recommend

import com.haeseong.nplusone.domain.config.ConfigService
import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.domain.item.detail.ItemDetailRepository
import com.haeseong.nplusone.domain.item.detail.ItemDetailVo
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface RecommendService {
    fun recommend(discountType: DiscountType): Slice<ItemDetailVo>

    fun recommend(storeType: StoreType): Slice<ItemDetailVo>
}

@Service
@Transactional(readOnly = true)
class RecommendServiceImpl(
    private val itemDetailRepository: ItemDetailRepository,
    private val configService: ConfigService,
) : RecommendService {

    // TODO: 추천 로직 적용
    override fun recommend(discountType: DiscountType): Slice<ItemDetailVo> {
        val referenceDate = configService.getReferenceDate()
        return itemDetailRepository.findByDiscountTypeAndReferenceDate(
            discountType = discountType,
            referenceDate = referenceDate,
            pageable = Pageable.ofSize(20)
        ).map { ItemDetailVo.from(it) }
    }

    // TODO: 추천 로직 적용
    override fun recommend(storeType: StoreType): Slice<ItemDetailVo> {
        val referenceDate = configService.getReferenceDate()
        return itemDetailRepository.findByStoreTypeAndReferenceDate(
            storeType = storeType,
            referenceDate = referenceDate,
            pageable = Pageable.ofSize(20)
        ).map { ItemDetailVo.from(it) }
    }

}