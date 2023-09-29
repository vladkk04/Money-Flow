package com.example.moneyflow.data.di

import android.app.Application
import com.example.moneyflow.data.local.db.TransactionDatabase
import com.example.moneyflow.data.local.repository.TransactionRepository
import com.example.moneyflow.domain.use_case.HomeUseCases
import com.example.moneyflow.domain.use_case.TransactionUseCases

object AppModule {
    private lateinit var appContext: Application

    private val database by lazy { TransactionDatabase.getDatabase(appContext) }
    private val repository by lazy { TransactionRepository(database.transactionDao()) }
    val transactionUseCase by lazy { TransactionUseCases.getInstance(repository) }

    fun init(application: Application) {
        appContext = application
    }

    val homeUseCases by lazy {
        HomeUseCases(
            transactionUseCase.getTransactionsUseCase,
            transactionUseCase.deleteTransactionUseCases
        )
    }

}