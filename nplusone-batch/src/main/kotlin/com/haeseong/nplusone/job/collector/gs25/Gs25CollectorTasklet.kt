package com.haeseong.nplusone.job.collector.gs25

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

open class Gs25CollectorTasklet(
    private val gs25CollectorService: Gs25CollectorService,
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val discountedItems = gs25CollectorService.getDiscountedItems()
        gs25CollectorService.validateAll(discountedItems = discountedItems)
        gs25CollectorService.saveAll(discountedItems = discountedItems)
        return RepeatStatus.FINISHED
    }
}