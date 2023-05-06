package com.haeseong.nplusone.job.collector.category

import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.domain.scrapping.ScrappingResult
import com.haeseong.nplusone.domain.scrapping.ScrappingResultRepository
import com.haeseong.nplusone.infrastructure.BatchConfig
import com.haeseong.nplusone.infrastructure.hibernate.JpaRepositoryPagingItemReader
import org.jsoup.Jsoup
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import java.time.LocalDate
import javax.persistence.EntityManagerFactory

@ConditionalOnProperty(
    value = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = CuCategoryCollectorConfig.JOB_NAME
)
@Configuration
class CuCategoryCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val scrappingResultRepository: ScrappingResultRepository,
    private val entityManagerFactory: EntityManagerFactory,
) {
    @Bean
    fun cuCategoryCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(cuCategoryCollectorStep(null))
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    @JobScope
    fun cuCategoryCollectorStep(
        @Value("#{jobParameters[referenceDate]}") referenceDate: String?,
    ): Step {
        return stepBuilderFactory[STEP_NAME]
            .chunk<ScrappingResult, ScrappingResult>(1000)
            .reader(
                JpaRepositoryPagingItemReader<ScrappingResult> { page, size ->
                    scrappingResultRepository.findByReferenceDateAndStoreTypeOrderByScrappingResultId(
                        LocalDate.parse(referenceDate!!),
                        StoreType.CU,
                        PageRequest.of(page, size),
                    ).content
                }
            )
            .processor(
                ItemProcessor<ScrappingResult, ScrappingResult> {
                    it.referenceUrl?.run {
                        it.addCategoryNames(getCategories(this))
                    }
                    it
                }
            )
            .writer(
                JpaItemWriterBuilder<ScrappingResult>()
                    .entityManagerFactory(entityManagerFactory)
                    .build()
            )
            .build()
    }

    private fun getCategories(url: String): Set<String> {
        val categories = mutableSetOf<String>()
        val document = Jsoup.connect(url).get()
        val category = document.select("#contents > div > ul > li.on > a").text()
        if (category.isNotBlank()) {
            categories.add(category)
        }
        categories.addAll(document.select("#taglist > li").map { it.text() })
        return categories
    }


    companion object {
        const val JOB_NAME = "cu-category-collector-job"
        const val STEP_NAME = "cu-category-collector-job-step"
    }
}
