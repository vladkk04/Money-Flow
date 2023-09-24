package com.example.moneyflow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyflow.data.Transaction
import com.example.moneyflow.databinding.TransactionRecycleViewRowBinding
import com.example.moneyflow.utils.DataPickerUtils
import java.util.Date

class TransactionAdapter(): ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(
    TransactionsComparator()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentTransaction = getItem(position)
        holder.binding.textViewExpense.text = currentTransaction.amount.toString()
        holder.binding.textViewDate.text = DataPickerUtils.currentDay(Date(currentTransaction.date))
        holder.binding.textViewCategoryTransaction.text = currentTransaction.category.name
        holder.binding.imageViewTypeTransaction.setImageResource(currentTransaction.category.icon)
    }

    class TransactionViewHolder(var binding: TransactionRecycleViewRowBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun create(parent: ViewGroup): TransactionViewHolder {
                val binding = TransactionRecycleViewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return TransactionViewHolder(binding)
            }
        }
    }

    class TransactionsComparator : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }


}