package com.haeseong.nplusone

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class NplusoneAdminApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application,admin")
    SpringApplication.run(NplusoneAdminApplication::class.java, *args)
}