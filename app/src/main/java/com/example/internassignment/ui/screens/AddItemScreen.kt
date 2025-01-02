package com.example.internassignment.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.navigation.NavHostController
import com.example.internassignment.model.Item
import com.example.internassignment.ui.common.CommonTopBar
import com.example.internassignment.ui.navigation.NavigationItem
import com.example.internassignment.viewmodel.ItemsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    viewModel: ItemsViewModel,
    navController: NavHostController
) {
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var shippingMethod by remember { mutableStateOf("") }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    val isValid = itemName.isNotEmpty() &&
            itemPrice.isNotEmpty() &&
            shippingMethod.isNotEmpty()

    val handleSubmit = {
        if (isValid) {
            val newItem = Item(
                name = itemName,
                price = itemPrice,
                // Convert "None" to empty string, keep other values as is
                extra = if (shippingMethod == "None") "" else shippingMethod
            )
            viewModel.addItem(newItem)
            navController.navigate(NavigationItem.ExploreGrid.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        } else {
            hasAttemptedSubmit = true
        }
    }

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
                        color = Color(0xFF8fd991),
                        modifier = Modifier.clickable() {
                            handleSubmit()
                        }
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
            onShippingMethodChange = { shippingMethod = it },
            onSubmit = handleSubmit,
            hasAttemptedSubmit = hasAttemptedSubmit,
            isValid = isValid
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
    onShippingMethodChange: (String) -> Unit,
    onSubmit: () -> Unit,
    hasAttemptedSubmit: Boolean,
    isValid: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedShippingMethod by remember { mutableStateOf(shippingMethod) }

    val pattern = remember { Regex("^\\d+\$") }

    // Define error states
    val showNameError = hasAttemptedSubmit && itemName.isEmpty()
    val showPriceError = hasAttemptedSubmit && itemPrice.isEmpty()
    val showShippingError = hasAttemptedSubmit && shippingMethod.isEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = itemName,
            onValueChange = onItemNameChange,
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            isError = showNameError,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                disabledContainerColor = colorScheme.surface,
            ),
        )

        OutlinedTextField(
            value = itemPrice,
            label = { Text("Item Price") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            onValueChange = {
                if (it.isEmpty() || it.matches(pattern)) {
                    onItemPriceChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = showPriceError,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                disabledContainerColor = colorScheme.surface,
            ),
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedShippingMethod,
                onValueChange = { },
                readOnly = true,
                label = { Text("Shipping Method") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                isError = showShippingError,
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Same Day Shipping") },
                    onClick = {
                        selectedShippingMethod = "Same Day Shipping"
                        onShippingMethodChange("Same Day Shipping")
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "None") },
                    onClick = {
                        selectedShippingMethod = "None"
                        onShippingMethodChange("None")
                        expanded = false
                    }
                )
            }
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5DB075),
            ),
        ) {
            Text("Submit", color = Color.White)
        }
    }
}



