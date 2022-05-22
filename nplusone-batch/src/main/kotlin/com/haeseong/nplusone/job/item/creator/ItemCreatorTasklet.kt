package com.haeseong.nplusone.job.item.creator

import com.haeseong.nplusone.domain.item.StoreType
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
        val storeType = resolveStoreType(chunkContext = chunkContext)
        itemCreatorService.createItems(referenceDate = referenceDate, storeType = storeType)
        return RepeatStatus.FINISHED
    }

    private fun resolveReferenceDate(chunkContext: ChunkContext): LocalDate {
        val referenceDate = chunkContext.stepContext.jobParameters["referenceDate"]?.toString()
            ?.let { LocalDate.parse(it) }
            ?: LocalDate.now()
        log.info("referenceDate: $referenceDate")
        return referenceDate
    }

    private fun resolveStoreType(chunkContext: ChunkContext): StoreType? {
        val storeType = chunkContext.stepContext.jobParameters["storeType"]?.toString()
            ?.let { StoreType.valueOf(it) }
        log.info("storeType: $storeType")
        return storeType
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ItemCreatorTasklet::class.java)
    }
}