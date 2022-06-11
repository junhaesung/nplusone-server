package com.haeseong.nplusone.job.item.detail

import com.haeseong.nplusone.domain.item.detail.ItemDetailService
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.infrastructure.BatchConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    value = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = ItemDetailCreatorJobConfig.JOB_NAME
)
@Configuration
class ItemDetailCreatorJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val scrappingResultService: ScrappingResultService,
    private val itemDetailService: ItemDetailService,
) {
    @Bean
    fun itemDetailCreatorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(itemDetailCreatorStep())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    @JobScope
    fun itemDetailCreatorStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(itemDetailCreatorTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun itemDetailCreatorTasklet(): Tasklet {
        return ItemDetailCreatorTasklet(
            itemDetailCreatorService = itemDetailCreatorService(),
        )
    }

    @Bean
    fun itemDetailCreatorService(): ItemDetailCreatorService = ItemDetailCreatorServiceImpl(
        scrappingResultService = scrappingResultService,
        itemDetailService = itemDetailService,
    )

    companion object {
        const val JOB_NAME = "item-detail-creator-job"
        private const val STEP_NAME = "item-detail-creator-step"
    }
}