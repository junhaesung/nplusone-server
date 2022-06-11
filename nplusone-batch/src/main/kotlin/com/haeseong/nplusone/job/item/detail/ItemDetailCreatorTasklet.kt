package com.haeseong.nplusone.job.item.detail

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import java.time.LocalDate

class ItemDetailCreatorTasklet(
    private val itemDetailCreatorService: ItemDetailCreatorService,
) : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val referenceDate = resolveReferenceDate(chunkContext)
        itemDetailCreatorService.create(referenceDate = referenceDate)
        return RepeatStatus.FINISHED
    }

    private fun resolveReferenceDate(chunkContext: ChunkContext): LocalDate {
        val referenceDate = chunkContext.stepContext.jobParameters["referenceDate"]
            ?.let { LocalDate.parse(it.toString()) }
            ?: LocalDate.now()
        log.info("referenceDate: $referenceDate")
        return referenceDate
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ItemDetailCreatorTasklet::class.java)
    }
}
