package com.example.moneyflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneyflow.data.repository.TransactionRepository
import com.example.moneyflow.utils.Category

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int ?= null,
    val amount: Double,
    val category: Category,
    val date: Long,
    val image: Int
)