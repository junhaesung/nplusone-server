package com.haeseong.nplusone.job.collector.cu

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class CuCollectorTasklet(
    private val cuCollectorService: CuCollectorService,
) : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val discountedItems = cuCollectorService.getDiscountedItems()
        log.info("discountedItems.size: ${discountedItems.size}")
        cuCollectorService.validateAll(discountedItems)
        cuCollectorService.saveAll(discountedItems)
        return RepeatStatus.FINISHED
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(CuCollectorTasklet::class.java)
    }
}