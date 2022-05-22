package com.haeseong.nplusone.job.item.similarity

import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.similarity.ItemSimilarityService
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
    havingValue = ItemSimilarityJobConfig.JOB_NAME
)
@Configuration
class ItemSimilarityJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val itemService: ItemService,
    private val itemSimilarityService: ItemSimilarityService,
) {
    @Bean
    fun itemSimilarityJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(itemSimilarityStep())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    @JobScope
    fun itemSimilarityStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(itemMergeTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun itemMergeTasklet(): Tasklet = ItemSimilarityTasklet(
        itemSimilarityFacadeService = itemSimilarityFacadeService()
    )

    @Bean
    fun itemSimilarityFacadeService(): ItemSimilarityFacadeService = ItemSimilarityFacadeServiceImpl(
        itemService = itemService,
        itemSimilarityService = itemSimilarityService,
    )

    companion object {
        const val JOB_NAME = "item-similarity-job"
        private const val STEP_NAME = "item-similarity-step"
    }
}