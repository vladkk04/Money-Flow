package com.example.moneyflow.use_case

import android.util.Log
import com.example.moneyflow.data.Transaction
import com.example.moneyflow.data.repository.TransactionRepository
import com.example.moneyflow.utils.Category
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

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