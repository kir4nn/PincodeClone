package com.example.internassignment.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.internassignment.ui.common.CommonSearchBar
import com.example.internassignment.ui.common.CommonTopBar
import com.example.internassignment.ui.components.ErrorScreen
import com.example.internassignment.ui.components.ListItemView
import com.example.internassignment.ui.components.LoadingScreen
import com.example.internassignment.viewmodel.ItemsUiState
import com.example.internassignment.viewmodel.ItemsViewModel

@Composable
fun ExploreListScreen(
    viewModel: ItemsViewModel = viewModel()
) {
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getItems()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CommonTopBar {
                CommonSearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it }
                )
            }
        },
    ) { paddingValues ->
        when (val itemsUiState = viewModel.itemsUiState) {
            is ItemsUiState.Loading -> LoadingScreen()
            is ItemsUiState.Success -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = 90.dp
                    )
            ) {
                items(itemsUiState.items) { item ->
                    ListItemView(item)
                }
            }

            is ItemsUiState.Error -> ErrorScreen()
        }
    }
}