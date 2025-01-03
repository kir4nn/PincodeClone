package com.example.internassignment.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.internassignment.R
import com.example.internassignment.ui.common.CommonSearchBar
import com.example.internassignment.ui.common.CommonTopBar
import com.example.internassignment.ui.components.ErrorScreen
import com.example.internassignment.ui.components.ListItemView
import com.example.internassignment.ui.components.LoadingScreen
import com.example.internassignment.viewmodel.ItemsUiState
import com.example.internassignment.viewmodel.ItemsViewModel

@Composable
fun ExploreListScreen(
    viewModel: ItemsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        viewModel.getItems()
    }

    LaunchedEffect(searchText, viewModel.itemsUiState) {
        if (viewModel.itemsUiState is ItemsUiState.Success && searchText.isNotEmpty()) {
            viewModel.filterItems(searchText)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CommonTopBar {
                CommonSearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    showFilterDialog = viewModel.showFilterDialog,
                    onFilterClick = { viewModel.toggleFilterDialog() },
                    priceRange = viewModel.priceRange,
                    onPriceRangeChange = { viewModel.updatePriceRange(it) },
                    sameDayShippingOnly = viewModel.sameDayShippingOnly,
                    onSameDayShippingChange = { viewModel.toggleSameDayShipping(it) },
                    onResetFilters = { viewModel.resetFilters() },
                )
            }
        },
    ) { paddingValues ->
        when (val itemsUiState = viewModel.itemsUiState) {
            is ItemsUiState.Loading -> {
                LoadingScreen(modifier = modifier.fillMaxSize())
            }

            is ItemsUiState.Success -> {
                if (itemsUiState.items.isEmpty() && searchText.isNotEmpty()) {
                    // Show no results found
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_items_found),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = paddingValues.calculateTopPadding(),
                                bottom = 80.dp
                            )
                    ) {
                        items(itemsUiState.items) { item ->
                            ListItemView(item)
                        }
                    }
                }
            }

            is ItemsUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
        }
    }
}