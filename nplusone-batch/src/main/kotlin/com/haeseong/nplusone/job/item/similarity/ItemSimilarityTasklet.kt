package com.haeseong.nplusone.job.item.similarity

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class ItemSimilarityTasklet(
    private val itemSimilarityFacadeService: ItemSimilarityFacadeService,
): Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        itemSimilarityFacadeService.calculateAllSimilarity()
        return RepeatStatus.FINISHED
    }
}