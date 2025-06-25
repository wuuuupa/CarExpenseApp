package com.example.carapp.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    val totalExpense = expenseDao.getTotalExpense()
    val totalFuelExpense = expenseDao.getTotalFuelExpense()
    val totalMileage = expenseDao.getTotalMileage()
    val firstDate = expenseDao.getFirstDate()
    
    suspend fun insert(expense: Expense) = expenseDao.insert(expense)
    suspend fun update(expense: Expense) = expenseDao.update(expense)
    suspend fun delete(expense: Expense) = expenseDao.delete(expense)
}
