package com.haeseong.nplusone.job.collector.seveneleven

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class SevenElevenCollectorTasklet(
    private val sevenElevenCollectorService: SevenElevenCollectorService,
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val discountedItems = sevenElevenCollectorService.getDiscountedItems()
        sevenElevenCollectorService.validateAll(discountedItems = discountedItems)
        sevenElevenCollectorService.saveAll(discountedItems = discountedItems)
        return RepeatStatus.FINISHED
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SevenElevenCollectorTasklet::class.java)
    }
}