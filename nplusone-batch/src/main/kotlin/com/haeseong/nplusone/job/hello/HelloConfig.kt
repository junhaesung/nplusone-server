package com.haeseong.nplusone.job.hello

import com.haeseong.nplusone.infrastructure.BatchConfig
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    value = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = HelloConfig.JOB_NAME
)
@Configuration
class HelloConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
) {
    @Bean
    fun helloJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(helloStep())
            .listener(helloListener())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    @JobScope
    fun helloStep(): Step {
        return stepBuilderFactory[STEP_NAME]
            .tasklet(helloTasklet())
            .transactionManager(ResourcelessTransactionManager())
            .build()
    }

    @Bean
    @StepScope
    fun helloTasklet() = HelloTasklet()

    @Bean
    @JobScope
    fun helloListener() = HelloListener()

    companion object {
        const val JOB_NAME = "hello-job"
        const val STEP_NAME = "hello-step"
    }
}