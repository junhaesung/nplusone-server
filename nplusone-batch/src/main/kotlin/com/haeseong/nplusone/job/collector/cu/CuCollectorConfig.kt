package com.haeseong.nplusone.job.collector.cu

import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.infrastructure.BatchConfig
import com.haeseong.nplusone.job.collector.CollectorService
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    value = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = CuCollectorConfig.JOB_NAME
)
@Configuration
class CuCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val scrappingResultService: ScrappingResultService
) {
    @Bean
    fun cuCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(cuCollectorStep())
            .build()
    }

    @Bean
    @JobScope
    fun cuCollectorStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(cuCollectorTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun cuCollectorTasklet(): Tasklet = CuCollectorTasklet(
        cuCollectorService = cuCollectorService(),
    )

    @Bean
    fun cuCollectorService(): CollectorService = CuCollectorService(
        scrappingResultService = scrappingResultService,
    )

    companion object {
        const val JOB_NAME = "cu-collector-job"
        const val STEP_NAME = "cu-collector-job-step"
    }
}

