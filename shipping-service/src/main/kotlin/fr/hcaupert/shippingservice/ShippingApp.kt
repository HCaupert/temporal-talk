package fr.hcaupert.shippingservice

import fr.hcaupert.temporalutils.TemporalConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@Import(TemporalConfiguration::class)
@SpringBootApplication
class ShippingApp

fun main(args: Array<String>) {
	runApplication<ShippingApp>(*args)
}
