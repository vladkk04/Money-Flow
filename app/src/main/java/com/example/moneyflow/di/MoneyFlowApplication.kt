package com.example.moneyflow.di

import android.app.Application
import com.example.moneyflow.data.TransactionDatabase
import com.example.moneyflow.data.repository.TransactionRepository

class MoneyFlowApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
    }
}