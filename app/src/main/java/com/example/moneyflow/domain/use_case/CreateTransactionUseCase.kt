package com.example.moneyflow.domain.use_case

import android.net.Uri
import com.example.moneyflow.data.local.entities.Transaction
import com.example.moneyflow.data.local.repository.TransactionRepository
import com.example.moneyflow.domain.model.Category

class CreateTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(amount: Double?, category: Category?, date: Long?, image: Uri?) {
        if (amount != null && category != null && date != null) {
            val transaction = Transaction(
                amount = amount,
                category = category,
                date = date,
                image = image ?: Uri.EMPTY
            )
            repository.insert(transaction)
        }
    }
}