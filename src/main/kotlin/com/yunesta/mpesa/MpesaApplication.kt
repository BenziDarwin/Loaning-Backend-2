package com.yunesta.mpesa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MpesaApplication

fun main(args: Array<String>) {
	runApplication<MpesaApplication>(*args)
}
