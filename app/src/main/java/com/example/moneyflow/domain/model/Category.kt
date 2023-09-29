package com.example.moneyflow.domain.model

import com.example.moneyflow.R

enum class Category(val id: Long, val icon: Int) {
    Food(0, R.drawable.ic_food),
    Pets(1, R.drawable.ic_pets),
    Other(2, R.drawable.ic_other);
}
