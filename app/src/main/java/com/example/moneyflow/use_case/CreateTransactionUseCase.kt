package com.example.moneyflow.use_case

import com.example.moneyflow.data.Transaction
import com.example.moneyflow.data.repository.TransactionRepository
import com.example.moneyflow.utils.Category

class CreateTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(amount: Double?, category: Category?, date: Long?, image: Int?) {
        if (amount != null && category != null && date != null) {
            val transaction = Transaction(
                amount = amount,
                category = category,
                date = date,
                image = image ?: 0
            )
            repository.insert(transaction)
        }
    }
}