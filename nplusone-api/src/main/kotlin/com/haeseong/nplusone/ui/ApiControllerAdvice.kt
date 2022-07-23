package com.haeseong.nplusone.ui

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
    @ExceptionHandler(
        BadRequestException::class,
        HttpMessageNotReadableException::class,
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(e: RuntimeException): ApiResponse<*> {
        log.warn("BAD_REQUEST", e)
        return ApiResponse.failure()
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): ApiResponse<*> {
        log.error("INTERNAL_SERVER_ERROR", e)
        return ApiResponse.failure()
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(ApiControllerAdvice::class.java)
    }
}