package com.example.internassignment.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internassignment.ui.components.FilterDialog

@Composable
fun CommonSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    showFilterDialog: Boolean,
    onFilterClick: () -> Unit,
    priceRange: ClosedFloatingPointRange<Float>,
    onPriceRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    sameDayShippingOnly: Boolean,
    onSameDayShippingChange: (Boolean) -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explore",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = "Filter",
                    fontSize = 16.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.clickable { onFilterClick() }
                )
            }

            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search", fontSize = 14.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(32.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            )
        }

        if (showFilterDialog) {
            FilterDialog(
                showDialog = showFilterDialog,
                onDismiss = onFilterClick,
                priceRange = priceRange,
                onPriceRangeChange = onPriceRangeChange,
                sameDayShippingOnly = sameDayShippingOnly,
                onSameDayShippingChange = onSameDayShippingChange,
                onCancel = onResetFilters,
            )
        }
    }
}