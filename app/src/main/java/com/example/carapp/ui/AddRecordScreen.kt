package com.example.carapp.ui

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carapp.data.Expense
import java.util.*

@Composable
fun AddRecordScreen(viewModel: ExpenseViewModel = viewModel()) {
    var type by remember { mutableStateOf("fuel") }
    var amount by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(Date()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("添加新记录", style = MaterialTheme.typography.headlineMedium)
        
        // 类型选择
        Text("支出类型:", style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = type == "fuel", 
                onClick = { type = "fuel" }
            )
            Text("油费", modifier = Modifier.padding(end = 16.dp))
            RadioButton(
                selected = type == "other", 
                onClick = { type = "other" }
            )
            Text("其他支出")
        }
        
        // 金额输入
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("金额 (¥)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        // 公里数输入
        OutlinedTextField(
            value = mileage,
            onValueChange = { mileage = it },
            label = { Text("公里数 (km)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        // 日期选择
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("日期: ${SimpleDateFormat("yyyy-MM-dd").format(date)}")
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, contentDescription = "选择日期")
            }
        }
        
        if (showDatePicker) {
            val calendar = Calendar.getInstance()
            calendar.time = date
            DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    calendar.set(year, month, day)
                    date = calendar.time
                    showDatePicker = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        
        // 保存按钮
        Button(
            onClick = {
                val expense = Expense(
                    type = type,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    mileage = mileage.toDoubleOrNull() ?: 0.0,
                    date = date
                )
                viewModel.insert(expense)
                // 清空表单
                amount = ""
                mileage = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = amount.isNotBlank() && mileage.isNotBlank()
        ) {
            Text("保存记录", fontSize = 18.sp)
        }
    }
}
