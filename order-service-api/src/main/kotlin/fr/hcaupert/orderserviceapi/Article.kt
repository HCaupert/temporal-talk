package fr.hcaupert.orderserviceapi

import java.util.*

typealias ArticleId = UUID
typealias Price = Long

data class Article(
    val id: ArticleId,
    val image: String,
    val name: String,
    val price: Price,
)
