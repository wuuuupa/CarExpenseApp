package com.example.carapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "fuel" or "other"
    val amount: Double,
    val mileage: Double,
    val date: Date
)
