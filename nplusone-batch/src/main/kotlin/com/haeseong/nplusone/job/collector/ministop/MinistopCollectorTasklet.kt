package com.haeseong.nplusone.job.collector.ministop

import com.haeseong.nplusone.job.collector.CollectorService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class MinistopCollectorTasklet(
    private val ministopCollectorService: CollectorService,
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val discountedItems = ministopCollectorService.getDiscountedItems()
        ministopCollectorService.validateAll(discountedItems = discountedItems)
        ministopCollectorService.saveAll(discountedItems = discountedItems)
        return RepeatStatus.FINISHED
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(MinistopCollectorTasklet::class.java)
    }
}