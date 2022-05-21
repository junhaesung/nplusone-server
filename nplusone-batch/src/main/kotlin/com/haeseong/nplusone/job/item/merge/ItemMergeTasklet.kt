package com.haeseong.nplusone.job.item.merge

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class ItemMergeTasklet(
    private val itemMergeService: ItemMergeService,
): Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        return RepeatStatus.FINISHED
    }
}