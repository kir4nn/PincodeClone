package com.example.internassignment.model

data class ApiResponse(
    val record: Record
)

data class Record(
    val status: String,
    val data: Data
)

data class Data(
    val items: List<Item>
)

data class Item(
    val name: String,
    val price: String,
    val extra: String
)