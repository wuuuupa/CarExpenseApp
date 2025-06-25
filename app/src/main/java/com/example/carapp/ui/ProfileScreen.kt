package com.example.carapp.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import kotlinx.coroutines.runBlocking

@Composable
fun ProfileScreen(viewModel: ExpenseViewModel = viewModel()) {
    var carName by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showClearDialog by remember { mutableStateOf(false) }
    
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("确认清除数据") },
            text = { Text("这将删除所有记录，不可恢复！") },
            confirmButton = {
                Button(
                    onClick = {
                        clearAllData(viewModel, context)
                        showClearDialog = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                Button(onClick = { showClearDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("我的设置", style = MaterialTheme.typography.headlineMedium)
        
        // 车辆名称设置
        OutlinedTextField(
            value = carName,
            onValueChange = { carName = it },
            label = { Text("车辆名称") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.saveCarName(carName, context)
                    Toast.makeText(context, "名称已保存", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Default.Check, contentDescription = "保存")
                }
            }
        )
        
        // 导出CSV按钮
        Button(
            onClick = { exportToCsv(context, viewModel) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("导出记录为CSV", fontSize = 16.sp)
        }
        
        // 清除数据按钮
        Button(
            onClick = { showClearDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("清除所有数据", fontSize = 16.sp, color = Color.White)
        }
    }
}

fun exportToCsv(context: Context, viewModel: ExpenseViewModel) {
    val expenses = runBlocking {
        viewModel.allExpenses.value ?: emptyList()
    }
    
    if (expenses.isEmpty()) {
        Toast.makeText(context, "没有数据可导出", Toast.LENGTH_SHORT).show()
        return
    }
    
    val file = File(context.getExternalFilesDir(null), "car_expenses_${System.currentTimeMillis()}.csv")
    val writer = CSVWriter(FileWriter(file))
    
    // 写入CSV头部
    writer.writeNext(arrayOf("类型", "金额", "公里数", "日期"))
    
    // 写入数据
    expenses.forEach { expense ->
        writer.writeNext(arrayOf(
            if (expense.type == "fuel") "油费" else "其他支出",
            expense.amount.toString(),
            expense.mileage.toString(),
            SimpleDateFormat("yyyy-MM-dd").format(expense.date)
        ))
    }
    
    writer.close()
    
    // 分享文件
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
    
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    
    context.startActivity(Intent.createChooser(shareIntent, "导出CSV文件"))
}

fun clearAllData(viewModel: ExpenseViewModel, context: Context) {
    runBlocking {
        viewModel.allExpenses.value?.forEach { expense ->
            viewModel.delete(expense)
        }
    }
    Toast.makeText(context, "数据已清除", Toast.LENGTH_SHORT).show()
}
