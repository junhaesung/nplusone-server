package com.haeseong.nplusone.job.collector.emart24

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
    havingValue = Emart24CollectorConfig.JOB_NAME
)
@Configuration
class Emart24CollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
) {
    @Bean
    fun emart24CollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(emart24CollectorStep())
            .build()
    }

    @Bean
    @JobScope
    fun emart24CollectorStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(emart24CollectorTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun emart24CollectorTasklet() = Emart24CollectorTasklet()

    companion object {
        const val JOB_NAME = "emart24-collector-job"
        const val STEP_NAME = "emart24-collector-step"
    }
}