package com.example.carapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.carapp.data.Expense
import com.example.carapp.data.ExpenseDatabase
import com.example.carapp.data.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExpenseRepository
    val allExpenses: LiveData<List<Expense>>
    val totalExpense: LiveData<Double>
    val totalFuelExpense: LiveData<Double>
    val totalMileage: LiveData<Double>
    val firstDate: LiveData<Date?>
    
    private val _carName = MutableLiveData<String>().apply {
        value = getCarNameFromPrefs(application)
    }
    val carName: LiveData<String> = _carName
    
    init {
        val dao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(dao)
        allExpenses = repository.allExpenses.asLiveData()
        totalExpense = repository.totalExpense.asLiveData()
        totalFuelExpense = repository.totalFuelExpense.asLiveData()
        totalMileage = repository.totalMileage.asLiveData()
        firstDate = repository.firstDate.asLiveData()
    }
    
    fun insert(expense: Expense) = viewModelScope.launch {
        repository.insert(expense)
    }
    
    fun update(expense: Expense) = viewModelScope.launch {
        repository.update(expense)
    }
    
    fun delete(expense: Expense) = viewModelScope.launch {
        repository.delete(expense)
    }
    
    fun saveCarName(name: String, context: Context) {
        _carName.value = name
        saveCarNameToPrefs(name, context)
    }
    
    private fun saveCarNameToPrefs(name: String, context: Context) {
        val prefs = context.getSharedPreferences("car_app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("car_name", name).apply()
    }
    
    private fun getCarNameFromPrefs(context: Context): String {
        val prefs = context.getSharedPreferences("car_app_prefs", Context.MODE_PRIVATE)
        return prefs.getString("car_name", "我的爱车") ?: "我的爱车"
    }
}
