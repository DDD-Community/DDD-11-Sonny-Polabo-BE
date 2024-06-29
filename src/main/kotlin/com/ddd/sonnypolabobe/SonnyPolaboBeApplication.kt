package com.ddd.sonnypolabobe

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SonnyPolaboBeApplication
inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!
fun main(args: Array<String>) {
    runApplication<SonnyPolaboBeApplication>(*args)
}
