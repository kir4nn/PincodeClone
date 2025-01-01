package com.example.internassignment.repository

import com.example.internassignment.model.Item

interface ItemsRepository {
    suspend fun getRemoteItems(): List<Item>
    suspend fun getLocalItems(): List<Item>
    suspend fun insertItem(item: Item)
    suspend fun getAllItems(): List<Item>
}