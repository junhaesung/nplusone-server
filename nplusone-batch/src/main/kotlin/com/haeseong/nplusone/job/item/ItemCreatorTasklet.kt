package com.haeseong.nplusone.job.item

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class ItemCreatorTasklet : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        log.info("hello item creator tasklet")
        return RepeatStatus.FINISHED
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ItemCreatorTasklet::class.java)
    }
}