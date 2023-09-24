package com.example.moneyflow.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.moneyflow.R
import com.example.moneyflow.databinding.ActivityMainBinding
import com.example.moneyflow.viewmodels.NavigationViewModel
import com.example.moneyflow.fragments.create.CreateTransactionViewModel
import com.example.moneyflow.viewmodels.createTransactionViewModel
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    private val navigationViewModel: NavigationViewModel by viewModels()
    private val createTransactionViewModel: CreateTransactionViewModel by createTransactionViewModel()

    private val createItemMenuView by lazy { binding.bottomNav.findViewById<View>(R.id.nav_create) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    createTransactionViewModel.uiState.collect { value ->
                        if (value.isCreationAllowed && !value.isCreated) {
                            createItemMenuView.setOnLongClickListener(createOnLongClickListener())
                            changeIconCreate(R.drawable.ic_done)
                        } else {
                            createItemMenuView.setOnLongClickListener(null)
                            changeIconCreate(R.drawable.ic_create)
                        }
                    }
                }
                launch {
                    navigationViewModel.uiState.collect { uiState ->
                        navigationViewModel.navigateTo(this@MainActivity, uiState.isNavigateTo)
                    }
                }
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            navigationViewModel.navigationItemSelected(item.itemId)
            true
        }
    }

    private fun createOnLongClickListener(): View.OnLongClickListener {
        return View.OnLongClickListener {
            createTransactionViewModel.createNewTransaction()
            true
        }
    }

    private fun changeIconCreate(icon: Int) {
        binding.bottomNav.menu.findItem(R.id.nav_create).setIcon(icon)
    }

}