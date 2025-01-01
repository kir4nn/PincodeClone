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
        return itemsApi.getItems().record.data.items
    }

    override suspend fun getLocalItems(): List<Item> {
        return itemDao.getAllItems().map { it.toItem() }
    }

    override suspend fun insertItem(item: Item) {
        itemDao.insertItem(item.toItemEntity())
    }

    override suspend fun getAllItems(): List<Item> {
        //combine remote and local items
        val remoteItems = try {
            getRemoteItems()
        } catch (e: Exception) {
            emptyList()
        }
        val localItems = getLocalItems()
        return remoteItems + localItems
    }
}

fun ItemEntity.toItem() = Item(name, price, extra)
fun Item.toItemEntity() = ItemEntity(name = name, price = price, extra = extra)
