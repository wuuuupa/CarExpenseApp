package com.example.carapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: Expense)
    
    @Update
    suspend fun update(expense: Expense)
    
    @Delete
    suspend fun delete(expense: Expense)
    
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>
    
    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpense(): Flow<Double>
    
    @Query("SELECT SUM(amount) FROM expenses WHERE type = 'fuel'")
    fun getTotalFuelExpense(): Flow<Double>
    
    @Query("SELECT MAX(mileage) FROM expenses")
    fun getTotalMileage(): Flow<Double>
    
    @Query("SELECT MIN(date) FROM expenses")
    fun getFirstDate(): Flow<Date?>
}
