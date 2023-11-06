package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.web.reactive.function.server.*

@SpringBootApplication
class DemoApplication {
	@Bean
	fun http(customerRepository: CustomerRepository) = coRouter {
		GET("customers/") {
			ServerResponse.ok().bodyAndAwait(customerRepository.findAll())
		}

		GET("customers/{id}") {
			val id = it.pathVariable("id").toInt()
			val result = customerRepository.findById(id)
			if(result != null)
				ServerResponse.ok().bodyValueAndAwait(result)
			else
				ServerResponse.notFound().buildAndAwait()

		}
	}
}

fun main(args: Array<String>) {
	// val: read-only
	// var: read and write
	//	println("hello world")
	runApplication<DemoApplication>(*args)
}

interface CustomerRepository: CoroutineCrudRepository<Customer, Int>

data class Customer(@Id val id: Int?, val name: String)
