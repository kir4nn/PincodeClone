package com.example.internassignment.repository

import com.example.internassignment.model.Item
import com.example.internassignment.model.ItemEntity
import com.example.internassignment.model.database.ItemDao
import com.example.internassignment.network.ItemsApiService

class ItemsRepositoryImpl(
    private val itemsApi: ItemsApiService,
    private val itemDao: ItemDao
) : ItemsRepository {
    override suspend fun getRemoteItems(): List<Item> {
        return try {
            val remoteItems = itemsApi.getItems().record.data.items
            itemDao.clearAllItems()
            itemDao.insertItems(remoteItems.map { item -> item.toItemEntity() })
            remoteItems
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getLocalItems(): List<Item> {
        return itemDao.getAllItems().map { it.toItem() }
    }

    override suspend fun insertItem(item: Item) {
        itemDao.insertItem(item.toItemEntity())
    }

    override suspend fun getAllItems(): List<Item> {
        // Get local items first
        val localItems = getLocalItems()

        // Try to fetch and merge remote items if possible
        return try {
            val remoteItems = getRemoteItems()
            if (remoteItems.isNotEmpty()) {
                // If we got remote items, they're already in the database
                getLocalItems()
            } else {
                // If remote fetch failed, use local items
                localItems
            }
        } catch (e: Exception) {
            // On error, return local items
            localItems
        }
    }

    override suspend fun refreshItems() {
        try {
            getRemoteItems() // This will clear and repopulate the database
        } catch (e: Exception) {
            // If refresh fails, keep existing local data
        }
    }
}

fun ItemEntity.toItem() = Item(name, price, extra)
fun Item.toItemEntity() = ItemEntity(name = name, price = price, extra = extra)
