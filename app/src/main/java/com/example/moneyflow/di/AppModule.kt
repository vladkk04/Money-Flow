package com.example.moneyflow.di

import android.app.Application
import com.example.moneyflow.data.TransactionDatabase
import com.example.moneyflow.data.repository.TransactionRepository
import com.example.moneyflow.use_case.HomeUseCases

object AppModule {
    private lateinit var appContext: Application

    fun init(application: Application) {
        appContext = application
    }

    private val database by lazy { TransactionDatabase.getDatabase(appContext) }
    private val repository by lazy { TransactionRepository(database.transactionDao()) }
    val transactionUseCase by lazy { TransactionUseCases.getInstance(repository) }

    val homeUseCases by lazy {
        HomeUseCases(
            transactionUseCase.getTransactionsUseCase,
            transactionUseCase.deleteTransactionUseCases
        )
    }

}