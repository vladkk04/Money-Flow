package com.example.moneyflow.data.local.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneyflow.domain.model.Category

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int ?= null,
    val amount: Double,
    val category: Category,
    val date: Long,
    val image: Uri
)