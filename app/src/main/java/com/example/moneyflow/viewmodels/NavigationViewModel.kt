package com.example.moneyflow.viewmodels

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import com.example.moneyflow.R
import com.example.moneyflow.activities.MainActivity
import com.example.moneyflow.fragments.base.BaseScreen
import com.example.moneyflow.fragments.create.CreateTransactionFragment
import com.example.moneyflow.fragments.home.HomeFragment
import com.example.moneyflow.fragments.settings.SettingsFragment
import com.example.moneyflow.utils.BottomNavigationPage
import com.example.moneyflow.models.NavigationUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(NavigationUIState(BottomNavigationPage.Home))
    val uiState get() = _uiState.asStateFlow()

    fun navigateTo(activity: MainActivity, isNavigateTo: BottomNavigationPage) {
        when (isNavigateTo) {
            BottomNavigationPage.Home -> launchFragment(activity, HomeFragment())
            BottomNavigationPage.Create -> launchFragment(activity, CreateTransactionFragment())
            BottomNavigationPage.Settings -> launchFragment(activity, SettingsFragment())
        }
    }

    fun navigationItemSelected(itemId: Int) {
        when (itemId) {
            BottomNavigationPage.Home.itemId -> {
                updateNavigation(BottomNavigationPage.Home)
            }
            BottomNavigationPage.Create.itemId -> {
                updateNavigation(BottomNavigationPage.Create)
            }
            BottomNavigationPage.Settings.itemId -> {
                updateNavigation(BottomNavigationPage.Settings)
            }
        }
    }

    private fun launchFragment(activity: MainActivity, screen: BaseScreen) {
        val fragment = screen.javaClass.newInstance() as Fragment
        val fragmentTag = screen.javaClass.canonicalName
        val existingFragment = activity.supportFragmentManager.findFragmentByTag(fragmentTag)

        if (existingFragment != null)
            return

        activity.supportFragmentManager.commit {
            replace(R.id.fv_container, fragment, fragmentTag)
        }
    }

    private fun updateNavigation(navigateTo: BottomNavigationPage) {
        _uiState.update {
            it.copy(
                isNavigateTo = navigateTo
            )
        }
    }
}