package com.example.internassignment.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val record: Record
)

@Serializable
data class Record(
    val status: String,
    val data: Data
)

@Serializable
data class Data(
    val items: List<Item>
)

@Serializable
data class Item(
    val name: String,
    val price: String,
    val extra: String
)