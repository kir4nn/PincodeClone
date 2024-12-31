package com.example.internassignment.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.internassignment.ui.common.CommonTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen() {
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var shippingMethod by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CommonTopBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Item",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),

                        )
                    Text(
                        text = "Add",
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
    ) { paddingValues ->
        AddItemContent(
            modifier = Modifier.padding(paddingValues),
            itemName = itemName,
            onItemNameChange = { itemName = it },
            itemPrice = itemPrice,
            onItemPriceChange = { itemPrice = it },
            shippingMethod = shippingMethod,
            onShippingMethodChange = { shippingMethod = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddItemContent(
    modifier: Modifier = Modifier,
    itemName: String,
    onItemNameChange: (String) -> Unit,
    itemPrice: String,
    onItemPriceChange: (String) -> Unit,
    shippingMethod: String,
    onShippingMethodChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedShippingMethod by remember { mutableStateOf(shippingMethod) }

    val pattern = remember { Regex("^\\d+\$") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Item Name Field
        OutlinedTextField(
            value = itemName,
            onValueChange = onItemNameChange,
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        )

        // Item Price Field
        OutlinedTextField(
            value = itemPrice,
            label = { Text("Item Price") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            onValueChange = {
                if (it.isNotEmpty() || it.matches(pattern)) {
                    onItemPriceChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Shipping Method Field (clickable to show dropdown)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedShippingMethod,
                onValueChange = {},
                readOnly = true,
                label = { Text("Shipping Method") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Same Day Shipping")
                    },
                    onClick = {
                        selectedShippingMethod = "Same Day Shipping"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "None")
                    },
                    onClick = {
                        selectedShippingMethod = "None"
                        expanded = false
                    }
                )
            }
        }

        // Submit Button
        Button(
            onClick = { /* Will handle later */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5DB075))
        ) {
            Text("Submit", color = Color.White)
        }
    }
}



