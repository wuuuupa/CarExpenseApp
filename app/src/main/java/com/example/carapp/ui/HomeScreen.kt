package com.example.carapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(viewModel: ExpenseViewModel = viewModel()) {
    val totalExpense by viewModel.totalExpense.observeAsState(0.0)
    val totalFuel by viewModel.totalFuelExpense.observeAsState(0.0)
    val totalMileage by viewModel.totalMileage.observeAsState(0.0)
    val firstDate by viewModel.firstDate.observeAsState(null)
    val carName by viewModel.carName.observeAsState("我的爱车")
    
    val daysTogether = firstDate?.let {
        val diff = System.currentTimeMillis() - it.time
        (diff / (1000 * 60 * 60 * 24)).toInt() + 1
    } ?: 0
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(carName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Spacer(Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                StatItem("相伴天数", "$daysTogether 天")
                Divider(Modifier.padding(vertical = 8.dp))
                StatItem("总支出", "¥${"%.2f".format(totalExpense)}")
                Divider(Modifier.padding(vertical = 8.dp))
                StatItem("油费总计", "¥${"%.2f".format(totalFuel)}")
                Divider(Modifier.padding(vertical = 8.dp))
                StatItem("支出/公里", 
                    if (totalMileage > 0) "¥${"%.2f".format(totalExpense / totalMileage)}/km" 
                    else "N/A")
                Divider(Modifier.padding(vertical = 8.dp))
                StatItem("油费/公里", 
                    if (totalMileage > 0) "¥${"%.2f".format(totalFuel / totalMileage)}/km" 
                    else "N/A")
                Divider(Modifier.padding(vertical = 8.dp))
                StatItem("成本/天", 
                    if (daysTogether > 0) "¥${"%.2f".format(totalExpense / daysTogether)}/天" 
                    else "N/A")
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}
