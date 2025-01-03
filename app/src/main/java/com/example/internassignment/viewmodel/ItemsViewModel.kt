package com.example.internassignment.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.internassignment.model.Item
import com.example.internassignment.model.database.ItemDatabase
import com.example.internassignment.network.ItemsApi
import com.example.internassignment.repository.ItemsRepositoryImpl
import kotlinx.coroutines.delay
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


    private var allItems: List<Item> = emptyList()

    var showFilterDialog by mutableStateOf(false)
        private set

    private val initialPriceRange = 0f..5000f
    private val initialSameDayShipping = false

    var priceRange by mutableStateOf(initialPriceRange)
        private set

    var sameDayShippingOnly by mutableStateOf(initialSameDayShipping)
        private set

    fun toggleFilterDialog() {
        showFilterDialog = !showFilterDialog
    }

    fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
        priceRange = range
        applyFilters()
    }

    fun toggleSameDayShipping(enabled: Boolean) {
        sameDayShippingOnly = enabled
        applyFilters()
    }

    fun resetFilters() {
        priceRange = initialPriceRange
        sameDayShippingOnly = initialSameDayShipping
        applyFilters()
    }

    private fun applyFilters() {
        itemsUiState = ItemsUiState.Loading
        val filteredItems = allItems.filter { item ->
            val itemPrice = item.price.toFloatOrNull() ?: 0f
            val priceInRange = itemPrice >= priceRange.start && itemPrice <= priceRange.endInclusive
            val shippingMatch = !sameDayShippingOnly || item.extra == "Same Day Shipping"
            priceInRange && shippingMatch
        }
        itemsUiState = ItemsUiState.Success(filteredItems)
    }

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            itemsUiState = ItemsUiState.Loading
            try {
                allItems = repository.getAllItems()
                // Only show error if we have no items at all
                itemsUiState = if (allItems.isEmpty()) {
                    ItemsUiState.Error
                } else {
                    ItemsUiState.Success(allItems)
                }
            } catch (e: Exception) {
                // If we have cached items, show them instead of error
                val localItems = repository.getLocalItems()
                itemsUiState = if (localItems.isNotEmpty()) {
                    allItems = localItems
                    ItemsUiState.Success(localItems)
                } else {
                    ItemsUiState.Error
                }
            }
        }
    }

    fun getItems() {
        viewModelScope.launch {
            itemsUiState = ItemsUiState.Loading
            try {
                allItems = repository.getAllItems()
                // Only show error if we have no items at all
                itemsUiState = if (allItems.isEmpty()) {
                    ItemsUiState.Error
                } else {
                    ItemsUiState.Success(allItems)
                }
            } catch (e: Exception) {
                // If we have cached items, show them instead of error
                val localItems = repository.getLocalItems()
                itemsUiState = if (localItems.isNotEmpty()) {
                    allItems = localItems
                    ItemsUiState.Success(localItems)
                } else {
                    ItemsUiState.Error
                }
            }
        }
    }

    fun refreshItems() {
        viewModelScope.launch {
            itemsUiState = ItemsUiState.Loading
            try {
                repository.refreshItems()
                loadItems()  // Reload items after refresh
            } catch (e: Exception) {
                // If refresh fails but we have cached items, show them
                val localItems = repository.getLocalItems()
                itemsUiState = if (localItems.isNotEmpty()) {
                    allItems = localItems
                    ItemsUiState.Success(localItems)
                } else {
                    ItemsUiState.Error
                }
            }
        }
    }

    fun filterItems(query: String) {
        itemsUiState = ItemsUiState.Loading
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
                // Immediately get local items to refresh the UI
                Log.d("AddItem", "Item added successfully: $item")
                val localItems = repository.getLocalItems()
                allItems = localItems
                itemsUiState = ItemsUiState.Success(localItems)
            } catch (e: Exception) {
                itemsUiState = ItemsUiState.Error
            }
        }
    }

}