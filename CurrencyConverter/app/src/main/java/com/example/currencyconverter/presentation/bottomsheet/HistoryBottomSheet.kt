package com.example.currencyconverter.presentation.bottomsheet

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.network.local.HistoryLocal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryBottomSheet(
    list: List<HistoryLocal>,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(true),
        containerColor = Color(0xFFFAFAFA),
        dragHandle = null) {
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .imePadding()
                .background(Color(0xFFFAFAFA))
                .padding(18.dp)
        ) {
            items(list) { item ->

                Divider(
                    thickness = 1.dp,
                    color = Color(0xFF3F51B5),
                    modifier = Modifier.fillMaxWidth().height(1.dp)
                )

                Text(
                    text = "${item.fromCurrency}\t ${item.from} \n${item.toCurrency}\t ${item.to}",
                    color = Color(0xFF3F51B5),
                    modifier = Modifier
                        .padding(vertical = 10.dp)

                )
            }
        }
    }
}