package com.haeseong.nplusone.job.collector.ministop

import com.haeseong.nplusone.infrastructure.BatchConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    value = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = MinistopCollectorConfig.JOB_NAME
)
@Configuration
class MinistopCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
) {
    @Bean
    fun ministopCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(ministopCollectorStep())
            .build()
    }

    @Bean
    @JobScope
    fun ministopCollectorStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(ministopCollectorTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun ministopCollectorTasklet() = MinistopCollectorTasklet()

    companion object {
        const val JOB_NAME = "ministop-collector-job"
        const val STEP_NAME = "ministop-collector-step"
    }
}