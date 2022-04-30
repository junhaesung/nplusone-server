package com.haeseong.nplusone.job.validdate

import com.haeseong.nplusone.domain.config.ConfigService
import com.haeseong.nplusone.infrastructure.BatchConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate


@ConditionalOnProperty(
    value = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = ValidDateJobConfig.JOB_NAME
)
@Configuration
class ValidDateJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val configService: ConfigService,
) {
    @Bean
    fun validDateJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(validDateStep())
            .build()
    }

    @Bean
    @JobScope
    fun validDateStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(validDateTasklet())
            .build()
    }

    @Bean
    @StepScope
    fun validDateTasklet(): Tasklet = Tasklet { _, chunkContext ->
        val validDate = chunkContext.stepContext.jobParameters["validDate"]
            ?.let { LocalDate.parse(it.toString()) }
            ?: LocalDate.now()
        configService.setValidDate(validDate)
        RepeatStatus.FINISHED
    }

    companion object {
        const val JOB_NAME = "valid-date-job"
        const val STEP_NAME = "valid-date-step"
    }
}