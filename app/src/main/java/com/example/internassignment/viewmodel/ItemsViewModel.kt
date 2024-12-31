package com.example.internassignment.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internassignment.model.Item
import com.example.internassignment.network.ItemsApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ItemsUiState {
    data class Success(val items: List<Item>) : ItemsUiState
    object Error : ItemsUiState
    object Loading : ItemsUiState
}

class ItemsViewModel : ViewModel() {
    var itemsUiState: ItemsUiState by mutableStateOf(ItemsUiState.Loading)
        private set

    init {
        getItems()
    }

    private fun getItems() {
        viewModelScope.launch {
            itemsUiState = ItemsUiState.Loading
            itemsUiState = try {
                val response = ItemsApi.retrofitService.getItems()
                val items = response.record.data.items
                ItemsUiState.Success(items)
            } catch (e: IOException) {
                ItemsUiState.Error
            } catch (e: HttpException) {
                ItemsUiState.Error
            }
        }
    }
}