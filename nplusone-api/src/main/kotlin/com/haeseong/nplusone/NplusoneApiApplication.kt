package com.haeseong.nplusone

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class NplusoneApiApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application,api")
    SpringApplication.run(NplusoneApiApplication::class.java, *args)
}