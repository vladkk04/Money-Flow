package com.example.moneyflow.domain.use_case

import android.util.Log
import com.example.moneyflow.data.local.entities.Transaction
import com.example.moneyflow.data.local.repository.TransactionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTransactionsUseCase(private val repository: TransactionRepository) {
    operator fun invoke(): Flow<List<Transaction>> = flow {
        try {
            repository.getAll.collect {
                emit(it)
            }
        } catch (e: CancellationException) {
            Log.d("x", "Error Emit")
        }
    }
}