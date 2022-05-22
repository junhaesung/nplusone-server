package com.haeseong.nplusone.domain.item.similarity

import org.springframework.stereotype.Component

interface SimilarityCalculator {
    fun calculate(source: String, target: String): Double
}

@Component
class SimilarityCalculatorImpl : SimilarityCalculator {
    override fun calculate(source: String, target: String): Double {
        val sourceNgram = ngram(source)
        val targetNgram = ngram(target)
        // FIXME: 중복된 원소 어떻게 계산해야하는지 확인 필요 (중복일 때 1번만 세기 or 중복일 때 중복되는만큼 세기)
        return sourceNgram.intersect(targetNgram).size.toDouble() / (sourceNgram + targetNgram).size
    }

    private fun ngram(source: String, number: Int = 2): Set<String> {
        val results = mutableSetOf<String>()
        val numberOfWords = source.length - number + 1
        (0 until numberOfWords).forEach {
            results += source.substring(it until it + number)
        }
        return results
    }
}