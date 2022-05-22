package com.haeseong.nplusone.domain.item.similarity

import com.haeseong.nplusone.domain.item.Item
import com.haeseong.nplusone.domain.item.ItemVo
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

/**
 * TODO: item 이름 변경되면 계산 다시해야함
 * TODO: item 삭제되면 유사도 삭제해야함
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class ItemSimilarity(
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(
        name = "SnowflakeIdGenerator",
        strategy = "com.haeseong.nplusone.infrastructure.generator.SnowflakeIdGenerator",
    )
    val itemSimilarity: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "sourceItemId")
    val sourceItem: Item,
    @ManyToOne
    @JoinColumn(name = "targetItemId")
    val targetItem: Item,
    var score: Double,
    var checked: Boolean = false,
) {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    fun calculate(similarityCalculator: SimilarityCalculator) {
        score = similarityCalculator.calculate(source = sourceItem.name, target = targetItem.name)
    }

    companion object {
        fun of(
            sourceItem: Item,
            targetItem: Item,
            similarityCalculator: SimilarityCalculator,
        ): ItemSimilarity {
            if (sourceItem.itemId >= targetItem.itemId) {
                throw IllegalArgumentException("sourceItemId must be less than targetItemId. sourceItemId: ${sourceItem.itemId}, targetItemId: ${targetItem.itemId}")
            }
            return ItemSimilarity(
                sourceItem = sourceItem,
                targetItem = targetItem,
                score = similarityCalculator.calculate(
                    source = sourceItem.name,
                    target = targetItem.name,
                ),
            )
        }
    }
}