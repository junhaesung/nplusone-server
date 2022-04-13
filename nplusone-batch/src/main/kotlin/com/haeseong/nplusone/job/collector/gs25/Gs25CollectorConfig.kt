package com.haeseong.nplusone.job.collector.gs25

import com.haeseong.nplusone.infrastructure.BatchConfig
import com.haeseong.nplusone.job.collector.cu.CuCollectorConfig
import com.haeseong.nplusone.job.collector.cu.CuCollectorTasklet
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
    havingValue = Gs25CollectorConfig.JOB_NAME
)
@Configuration
class Gs25CollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
) {
    @Bean
    fun gs25CollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(gs25CollectorStep())
            .build()
    }

    @Bean
    @JobScope
    fun gs25CollectorStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(gs25CollectorTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun gs25CollectorTasklet() = Gs25CollectorTasklet()

    companion object {
        const val JOB_NAME = "gs25-collector-job"
        const val STEP_NAME = "gs25-collector-step"
    }
}