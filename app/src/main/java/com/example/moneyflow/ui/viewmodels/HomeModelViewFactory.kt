package com.example.moneyflow.ui.viewmodels

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneyflow.data.di.AppModule
import com.example.moneyflow.ui.fragments.home.HomeFragment
import com.example.moneyflow.ui.fragments.home.HomeViewModel
import com.example.moneyflow.domain.use_case.HomeUseCases

class HomeModelViewFactory(private val homeUseCases: HomeUseCases): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(homeUseCases) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

inline fun <reified VM : ViewModel> HomeFragment.homeViewModel() = activityViewModels<VM> {
    HomeModelViewFactory(AppModule.homeUseCases)
}