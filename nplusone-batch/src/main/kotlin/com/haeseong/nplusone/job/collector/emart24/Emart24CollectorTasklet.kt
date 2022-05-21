package com.haeseong.nplusone.job.collector.emart24

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class Emart24CollectorTasklet(
    private val emart24CollectorService: Emart24CollectorService,
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val discountedItems = emart24CollectorService.getDiscountedItems()
        emart24CollectorService.validateAll(discountedItems = discountedItems)
        emart24CollectorService.saveAll(discountedItems = discountedItems)
        return RepeatStatus.FINISHED
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(Emart24CollectorTasklet::class.java)
    }
}