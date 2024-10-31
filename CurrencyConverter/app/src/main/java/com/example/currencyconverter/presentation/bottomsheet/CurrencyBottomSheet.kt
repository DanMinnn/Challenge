package com.example.currencyconverter.presentation.bottomsheet

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyBottomSheet(
    onDismiss: () -> Unit,
    selectedCurrency: (selected: String) -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }

    var filteredCurrencyList = Constants.CurrencyCodeList.filter {
        it.currencyCode.contains(searchQuery, ignoreCase = true) ||
                it.countryName.contains(searchQuery, ignoreCase = true)
    }
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(true),
        containerColor = Color(0xFFFAFAFA),
        dragHandle = null
    ) {

        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = "Search currency",
                    color = Color(0xFF7A7A7A)
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp,bottom = 16.dp, end = 20.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFF9E9E9E),
                focusedBorderColor = Color(0xFF3F51B5)
            )
        )

        LazyColumn(
            modifier = Modifier
                .imePadding()
                .background(Color(0xFFFAFAFA))
                .padding(18.dp)
        ) {
            items(filteredCurrencyList) { item ->
                Text(
                    text = "${item.currencyCode}\t ${item.countryName}",
                    color = Color(0xFF3F51B5),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .clickable {
                            selectedCurrency(item.currencyCode)
                        }
                )

                Divider(
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )

            }
        }
    }
}


