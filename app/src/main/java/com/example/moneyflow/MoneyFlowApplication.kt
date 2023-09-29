package com.example.moneyflow

import android.app.Application
import com.example.moneyflow.data.di.AppModule

class MoneyFlowApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
    }
}