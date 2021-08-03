package com.katyrin.dictionaryapp.view.main

import android.os.Bundle
import androidx.core.view.isVisible
import com.katyrin.dictionaryapp.R
import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.databinding.ActivityMainBinding
import com.katyrin.dictionaryapp.view.base.BaseActivity
import com.katyrin.dictionaryapp.view.main.adapter.MainAdapter
import com.katyrin.dictionaryapp.viewmodel.MainViewModel
import com.katyrin.dictionaryapp.viewmodel.interactor.MainInteractor
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<AppState, MainInteractor>() {

    override lateinit var model: MainViewModel

    private var binding: ActivityMainBinding? = null
    private var adapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        iniViewModel()
        binding?.searchFab?.setOnClickListener {
            SearchDialogFragment.newInstance(supportFragmentManager).setOnSearchClickListener(
                object : SearchDialogFragment.OnSearchClickListener {
                    override fun onClick(searchWord: String) {
                        isNetworkAvailable = networkState.isOnline()
                        if (isNetworkAvailable) {
                            model.getData(searchWord, isNetworkAvailable)
                        } else {
                            showNoInternetConnectionDialog()
                        }
                    }
                }
            )
        }
    }

    private fun iniViewModel() {
        if (binding?.mainActivityRecyclerview?.adapter != null)
            throw IllegalStateException("The ViewModel should be initialised first")
        val viewModel: MainViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(this@MainActivity) { renderData(it) }
    }

    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                val data = appState.data
                if (data.isNullOrEmpty()) {
                    showAlertDialog(
                        getString(R.string.dialog_title_sorry),
                        getString(R.string.empty_server_response_on_success)
                    )
                } else {
                    adapter?.setData(data)
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    binding?.progressBarHorizontal?.isVisible = true
                    binding?.progressBarRound?.isVisible = false
                    binding?.progressBarHorizontal?.progress = appState.progress
                } else {
                    binding?.progressBarHorizontal?.isVisible = false
                    binding?.progressBarRound?.isVisible = true
                }
            }
            is AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }

    private fun showViewWorking() {
        binding?.loadingFrameLayout?.isVisible = false
    }

    private fun showViewLoading() {
        binding?.loadingFrameLayout?.isVisible = true
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}