package com.example.internassignment.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.internassignment.model.Item
import com.example.internassignment.model.database.ItemDatabase
import com.example.internassignment.network.ItemsApi
import com.example.internassignment.repository.ItemsRepositoryImpl
import kotlinx.coroutines.launch

sealed interface ItemsUiState {
    data class Success(val items: List<Item>) : ItemsUiState
    object Error : ItemsUiState
    object Loading : ItemsUiState
}

class ItemsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ItemsRepositoryImpl(
        ItemsApi.retrofitService,
        ItemDatabase.getDatabase(application).itemDao()
    )

    var itemsUiState: ItemsUiState by mutableStateOf(ItemsUiState.Loading)
        private set

    // Store the original unfiltered items
    private var allItems: List<Item> = emptyList()

    init {
        getItems()
    }

    fun getItems() {
        viewModelScope.launch {
            itemsUiState = ItemsUiState.Loading
            itemsUiState = try {
//                val response = ItemsApi.retrofitService.getItems()
//                val items = response.record.data.items
                allItems = repository.getAllItems()
                ItemsUiState.Success(allItems)
            } catch (e: Exception) {
                ItemsUiState.Error
            }
        }
    }

    fun filterItems(query: String) {
        if (query.isEmpty()) {
            itemsUiState = ItemsUiState.Success(allItems)
            return
        }
        val filteredItems = allItems.filter { item ->
            item.name.contains(query, ignoreCase = true)
        }
        itemsUiState = ItemsUiState.Success(filteredItems)
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            try {
                repository.insertItem(item)
                getItems()
            } catch (e: Exception) {
                itemsUiState = ItemsUiState.Error
            }
        }
    }
}