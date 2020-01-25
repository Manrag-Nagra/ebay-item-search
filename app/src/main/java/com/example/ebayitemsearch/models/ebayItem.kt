package com.example.ebayitemsearch.models

data class Item(
    val href: String,
    val itemSummaries: MutableList<ItemSummary>
)

data class ItemSummary(
    val itemId: String,
    val title: String,
    val image: Image,
    val price: Price,
    val seller: Seller
)

data class Image(
    val imageUrl: String
)

data class Price(
    val value: String,
    val currency: String
)

data class Seller(
    val username: String,
    val feedbackPercentage: String,
    val feedbackScore: Int
)