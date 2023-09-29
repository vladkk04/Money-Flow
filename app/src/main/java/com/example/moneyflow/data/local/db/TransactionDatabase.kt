package com.example.moneyflow.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moneyflow.data.local.dao.TransactionDao
import com.example.moneyflow.data.local.entities.Transaction

@Database(version = 1, entities = [Transaction::class], exportSchema = false)
@TypeConverters(Converters::class)
abstract class TransactionDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object{
        @Volatile
        private var INSTANCE: TransactionDatabase?= null

        fun getDatabase(context: Context): TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction-database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}