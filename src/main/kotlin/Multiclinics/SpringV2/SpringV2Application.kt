package Multiclinics.SpringV2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class SpringV2Application

fun main(args: Array<String>) {
	runApplication<SpringV2Application>(*args)
}

