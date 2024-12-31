package com.example.internassignment.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.internassignment.model.Item

@Composable
fun ListItemView(item: Item) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "MRP: ₹${item.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        if (item.extra.isNotEmpty()) {
            Text(
                text = item.extra,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

