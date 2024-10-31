package com.example.currencyconverter.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter.R
import com.example.currencyconverter.models.CurrencyConvert
import com.example.currencyconverter.presentation.bottomsheet.CurrencyBottomSheet
import com.example.currencyconverter.presentation.bottomsheet.HistoryBottomSheet
import com.example.currencyconverter.presentation.components.DropDown
import com.example.currencyconverter.presentation.viewmodel.CurrencyViewModel
import com.example.currencyconverter.ui.theme.inter
import com.example.currencyconverter.utils.Constants.Companion.DEFAULT
import com.example.currencyconverter.utils.Constants.Companion.FROM
import com.example.currencyconverter.utils.Constants.Companion.TO
import com.example.currencyconverter.utils.NetworkResult

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(
    context: Context, viewModel: CurrencyViewModel
) {

    val uiState by viewModel.state.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showHistoryBottomSheet by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf("") }


    // Function to validate the input
    fun validateAmount(amount: String): Boolean {
        //if (amount.isEmpty()) return true
        return amount.toDoubleOrNull() != null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Currency Converter",
                        fontSize = 24.sp,
                        fontFamily = inter,
                        color = Color.Black,
                        style = TextStyle(
                            fontWeight = FontWeight.Black,
                            lineHeight = 30.sp
                        )
                    )
                },
            )
        },
    ) { _ ->
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),

            ) {

            Spacer(modifier = Modifier
                .padding(48.dp)
                .height(30.dp))

            // Loading indicator
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            // Display Error message
            if (uiState.networkResult is NetworkResult.Error) {
                Text(
                    text = (uiState.networkResult as NetworkResult.Error).message ?: "An error occurred",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }

            DropDown(
                value = uiState.defaultCurrency,
                label = stringResource(id = R.string.select_your_default_preference),
                clicked = {
                    type = DEFAULT
                    showBottomSheet = true
                },
                updateValue = {
                    type = DEFAULT
                    showBottomSheet = true
                },
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Row {
                DropDown(
                    value = uiState.baseCurrency,
                    label = FROM,
                    clicked = {
                        type = FROM
                        showBottomSheet = true
                    },
                    updateValue = {
                        type = FROM
                        showBottomSheet = true
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                DropDown(
                    value = uiState.toCurrency,
                    label = TO,
                    clicked = {
                        type = TO
                        showBottomSheet = true
                    },
                    updateValue = {
                        type = TO
                        showBottomSheet = true
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))

            val keyBoardController = LocalSoftwareKeyboardController.current
            var isValidAmount by remember { mutableStateOf(true) }

            OutlinedTextField(
                label = {
                    Text(
                        text = stringResource(R.string.amount),
                        color = Color.Black
                    )
                },
                value = uiState.amount ?: "",
                onValueChange = {
                    viewModel.updateAmount(it)
                    isValidAmount = validateAmount(it)
                },
                maxLines = 1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            keyBoardController?.hide()
                            isValidAmount = validateAmount(uiState.amount ?: "")
                        }
                    },

                keyboardActions = KeyboardActions(
                    onDone = {
                        keyBoardController?.hide()
                        isValidAmount = validateAmount(uiState.amount ?: "")
                    }
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = !isValidAmount
            )

            if (!isValidAmount) {
                Text(
                    text = "Please enter a valid amount",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))

            Row {
                Button(
                    onClick = {
                        viewModel.doCalculation()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    ),
                ) {
                    Text(
                        stringResource(R.string.add_to_history),
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = {
                        viewModel.swapTargets()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    ),
                ) {
                    Text(
                        stringResource(R.string.swap_currencies),
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.padding(12.dp))

            // Show history when network result is Success
           if (uiState.networkResult is NetworkResult.Success && uiState.historyList.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.view_history),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            showHistoryBottomSheet = true
                        },
                    fontSize = 20.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.padding(15.dp))
            }
            Text(
                text = uiState.convertedValue,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 30.sp,
                color = Color.Black,
                style = TextStyle(
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = uiState.singleConvertedValue,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                style = TextStyle(textAlign = TextAlign.Center)
            )

        }

    }

    Spacer(modifier = Modifier.height(30.dp))

    if (showBottomSheet) {
        CurrencyBottomSheet(
            onDismiss = {
                showBottomSheet = false
            },
            selectedCurrency = {
                viewModel.updateDropDownValues(countryCurrencyCode = it, isForBase = type)
                showBottomSheet = false

            }
        )
    }

    if (showHistoryBottomSheet) {
        HistoryBottomSheet(
            list = uiState.historyList,
            onDismiss = {
                showHistoryBottomSheet = false
            },
        )
    }
}

