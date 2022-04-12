package com.haeseong.nplusone

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.time.LocalDateTime

@SpringBootApplication
class NplusoneBatchApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application,batch")
    SpringApplication.run(NplusoneBatchApplication::class.java, *(args + "startTime=${LocalDateTime.now()}"))
}