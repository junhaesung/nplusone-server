package com.haeseong.nplusone.job.collector.seveneleven

import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.infrastructure.BatchConfig
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
    havingValue = SevenElevenCollectorConfig.JOB_NAME
)
@Configuration
class SevenElevenCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val scrappingResultService: ScrappingResultService,
) {
    @Bean
    fun sevenElevenCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(sevenElevenCollectorStep())
            .build()
    }

    @Bean
    @JobScope
    fun sevenElevenCollectorStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(sevenElevenCollectorTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun sevenElevenCollectorTasklet(): Tasklet = SevenElevenCollectorTasklet(
        sevenElevenCollectorService = sevenElevenCollectorService(),
    )

    @Bean
    fun sevenElevenCollectorService(): SevenElevenCollectorService = SevenElevenCollectorServiceImpl(
        scrappingResultService = scrappingResultService,
    )

    companion object {
        const val JOB_NAME = "seven-eleven-collector-job"
        const val STEP_NAME = "seven-eleven-collector-step"
    }
}