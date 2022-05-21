package com.haeseong.nplusone.domain.scrapping

class ScrappingResultDuplicatedException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)