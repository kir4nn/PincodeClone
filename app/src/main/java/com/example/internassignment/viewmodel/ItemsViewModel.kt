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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface ItemsUiState {
    data class Success(val items: List<Item>) : ItemsUiState
    data object Error : ItemsUiState
    data object Loading : ItemsUiState
}

class ItemsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ItemsRepositoryImpl(
        ItemsApi.retrofitService,
        ItemDatabase.getDatabase(application).itemDao()
    )

    private val _itemsUiState = MutableStateFlow<ItemsUiState>(ItemsUiState.Loading)
    val itemsUiState: StateFlow<ItemsUiState> = _itemsUiState.asStateFlow()

    // Cache management
    private var lastFetchTime: Long = 0
    private val cacheTimeout = 1 * 60 * 1000 // 5 minutes
    private var cachedItems: List<Item> = emptyList()


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
    }

    fun toggleSameDayShipping(enabled: Boolean) {
        sameDayShippingOnly = enabled
    }

    fun resetFilters() {
        priceRange = initialPriceRange
        sameDayShippingOnly = initialSameDayShipping
        applyFilters()
    }

    fun applyFilters() {
        _itemsUiState.value = ItemsUiState.Loading
        val filteredItems = cachedItems.filter { item ->
            val itemPrice = item.price.toFloatOrNull() ?: 0f
            val priceInRange = itemPrice >= priceRange.start && itemPrice <= priceRange.endInclusive
            val shippingMatch = !sameDayShippingOnly || item.extra == "Same Day Shipping"
            priceInRange && shippingMatch
        }
        _itemsUiState.value = ItemsUiState.Success(filteredItems)
    }

    init {
        getItems()
    }

    fun getItems(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _itemsUiState.value = ItemsUiState.Loading

            // Check if we have valid cached data
            if (!forceRefresh && System.currentTimeMillis() - lastFetchTime < cacheTimeout && cachedItems.isNotEmpty()) {
                _itemsUiState.value = ItemsUiState.Success(cachedItems)
                return@launch
            }

            try {
                withContext(Dispatchers.IO) {
                    val items = repository.getAllItems()
                    lastFetchTime = System.currentTimeMillis()
                    cachedItems = items

                    withContext(Dispatchers.Main) {
                        if (items.isEmpty()) {
                            _itemsUiState.value = ItemsUiState.Error
                        } else {
                            _itemsUiState.value = ItemsUiState.Success(items)
                        }
                    }
                }
            } catch (e: Exception) {
                _itemsUiState.value = ItemsUiState.Error
            }
        }
    }

    fun filterItems(query: String) {
        _itemsUiState.value = ItemsUiState.Loading
        if (query.isEmpty()) {
            _itemsUiState.value = ItemsUiState.Success(cachedItems)
            return
        }
        val filteredItems = cachedItems.filter { item ->
            item.name.contains(query, ignoreCase = true)
        }
        _itemsUiState.value = ItemsUiState.Success(filteredItems)
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            try {
                repository.insertItem(item)
                getItems(forceRefresh = true)
            } catch (e: Exception) {
                _itemsUiState.value = ItemsUiState.Error
            }
        }
    }

}