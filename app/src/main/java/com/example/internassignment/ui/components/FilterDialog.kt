package com.example.internassignment.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    priceRange: ClosedFloatingPointRange<Float>,
    onPriceRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    sameDayShippingOnly: Boolean,
    onSameDayShippingChange: (Boolean) -> Unit,
    onSubmit: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Filter") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    RangeSlider(
                        value = priceRange,
                        onValueChange = onPriceRangeChange,
                        valueRange = 0f..5000f,
                        steps = (5000f / 50f).toInt() - 1
                    )
                    Text("Price: ₹${priceRange.start.toInt()} - ₹${priceRange.endInclusive.toInt()}")

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Same Day Shipping")
                        Switch(
                            checked = sameDayShippingOnly,
                            onCheckedChange = onSameDayShippingChange
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick =
                    onDismiss
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}