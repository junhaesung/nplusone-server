package com.haeseong.nplusone.job.item.merge

import com.haeseong.nplusone.domain.item.ItemService
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
    havingValue = ItemMergeJobConfig.JOB_NAME
)
@Configuration
class ItemMergeJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val itemService: ItemService,
) {
    @Bean
    fun itemMergeJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(itemMergeStep())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    @JobScope
    fun itemMergeStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(itemMergeTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun itemMergeTasklet(): Tasklet = ItemMergeTasklet(
        itemMergeService = itemMergeService()
    )

    @Bean
    fun itemMergeService(): ItemMergeService = ItemMergeServiceImpl(
        itemService = itemService,
    )

    companion object {
        const val JOB_NAME = "item-merge-job"
        private const val STEP_NAME = "item-merge-step"
    }
}