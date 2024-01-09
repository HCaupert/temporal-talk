package fr.hcaupert.gateway

import com.fasterxml.jackson.databind.JsonNode
import fr.hcaupert.orderserviceapi.*
import jakarta.annotation.PostConstruct
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDateTime
import java.util.*


@Service
class RandomCreator {

    val faker = Faker()


    @Value("\${app.azure-secret}")
    lateinit var secret: String

    lateinit var restTemplate: RestTemplate

    @PostConstruct
    fun init() {
        restTemplate = RestTemplateBuilder()
            .defaultHeader("Ocp-Apim-Subscription-Key", secret)
            .build()
    }


    fun order(): Order {
        val title = faker.videoGame().title()

        val uri = UriComponentsBuilder.fromHttpUrl("https://api.bing.microsoft.com/v7.0/images/search")
            .queryParam("q", title)
            .build()
            .toUri()
        val imageUrl =
            restTemplate.getForObject(uri, JsonNode::class.java)?.get("value")?.get(0)?.get("contentUrl")?.asText()
                .orEmpty()


        return Order(
            id = UUID.randomUUID(),
            creationDate = LocalDateTime.now(),
            article = Article(
                id = UUID.randomUUID(),
                image = imageUrl,
                name = title,
                price = faker.numerify("####").toLong(),
            ),
            payment = Payment(id = UUID.randomUUID()),
            shipping = ShippingDetails(
                method = ShippingDetails.ShippingMethod.random(),
                address = Address(
                    street = faker.address().streetName(),
                    city = faker.address().city(),
                    iso3CountryCode = faker.address().countryCode(),
                ),
                receiver = Receiver(
                    firstName = faker.name().firstName(),
                    lastName = faker.name().lastName(),
                ),
            )
        )
    }
}

