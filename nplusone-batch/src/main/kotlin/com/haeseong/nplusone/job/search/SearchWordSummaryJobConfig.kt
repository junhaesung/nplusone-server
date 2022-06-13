package com.haeseong.nplusone.job.search

import com.haeseong.nplusone.domain.search.summary.SearchSummaryService
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
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@ConditionalOnProperty(
    value = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = SearchWordSummaryJobConfig.JOB_NAME
)
@Configuration
class SearchWordSummaryJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val searchSummaryService: SearchSummaryService,
) {

    @Bean
    fun searchWordSummaryJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(searchWordSummaryStep())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    @JobScope
    fun searchWordSummaryStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(searchWordSummaryTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun searchWordSummaryTasklet() = Tasklet { _, chunkContext ->
        val referenceDateTime = chunkContext.stepContext.jobParameters["referenceDateTime"]
            ?.let { LocalDateTime.parse(it.toString()) }
            ?: LocalDate.now().atTime(LocalTime.now().hour, 0)
        searchSummaryService.create(referenceDateTime)
        RepeatStatus.FINISHED
    }

    companion object {
        const val JOB_NAME = "search-word-summary-job"
        private const val STEP_NAME = "search-word-summary-step"
    }
}