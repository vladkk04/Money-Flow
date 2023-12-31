package com.example.moneyflow.data.local.repository

import com.example.moneyflow.data.local.dao.TransactionDao
import com.example.moneyflow.data.local.entities.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    val getAll: Flow<List<Transaction>> = transactionDao.getAll()

    suspend fun insert(transaction: Transaction) = transactionDao.insert(transaction)

    suspend fun update(transaction: Transaction) = transactionDao.update(transaction)

    suspend fun delete(transaction: Transaction) = transactionDao.delete(transaction)

}