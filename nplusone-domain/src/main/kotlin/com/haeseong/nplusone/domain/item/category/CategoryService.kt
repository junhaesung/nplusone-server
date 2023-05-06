package com.haeseong.nplusone.domain.item.category

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface CategoryService {
    fun create(categoryCreateVo: CategoryCreateVo): Category
    fun findByName(name: String): Category?
}

@Service
@Transactional(readOnly = true)
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
) : CategoryService {
    @Transactional
    override fun create(categoryCreateVo: CategoryCreateVo): Category {
        val category = categoryRepository.findByName(name = categoryCreateVo.name)
        if (category != null) {
            return category
        }
        return categoryRepository.save(Category.from(categoryCreateVo))
    }

    override fun findByName(name: String): Category? {
        return categoryRepository.findByName(name)
    }
}
