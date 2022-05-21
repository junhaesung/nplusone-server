package com.haeseong.nplusone.job.item

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import java.time.LocalDate

class ItemCreatorTasklet(
    private val itemCreatorService: ItemCreatorService,
) : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val referenceDate = resolveReferenceDate(chunkContext = chunkContext)
        log.info("referenceDate: $referenceDate")
        itemCreatorService.createItems(referenceDate = referenceDate)
        return RepeatStatus.FINISHED
    }

    private fun resolveReferenceDate(chunkContext: ChunkContext): LocalDate {
        return chunkContext.stepContext.jobParameters["referenceDate"]?.toString()
            ?.let { LocalDate.parse(it) }
            ?: LocalDate.now()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ItemCreatorTasklet::class.java)
    }
}