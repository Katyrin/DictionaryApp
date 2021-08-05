package com.katyrin.dictionaryapp.view.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.R
import com.katyrin.dictionaryapp.databinding.ActivityMainBinding
import com.katyrin.dictionaryapp.viewmodel.interactor.MainInteractor
import com.katyrin.dictionaryapp.view.base.BaseActivity
import com.katyrin.dictionaryapp.view.main.adapter.MainAdapter
import com.katyrin.dictionaryapp.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity<AppState, MainInteractor>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    override val viewModel: MainViewModel by viewModels(factoryProducer = { factory })

    private var binding: ActivityMainBinding? = null
    private var adapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.searchFab?.setOnClickListener {
            SearchDialogFragment.newInstance(supportFragmentManager).setOnSearchClickListener(
                object : SearchDialogFragment.OnSearchClickListener {
                    override fun onClick(searchWord: String) {
                        viewModel
                            .getData(searchWord, true)
                            .observe(this@MainActivity) { renderData(it) }
                    }
                }
            )
        }
    }

    override fun renderData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                val dataModel = state.data
                if (dataModel == null || dataModel.isEmpty()) {
                    showErrorScreen(getString(R.string.empty_server_response_on_success))
                } else {
                    showViewSuccess()
                    if (adapter == null) {
                        binding?.mainActivityRecyclerview?.layoutManager =
                            LinearLayoutManager(applicationContext)
                        binding?.mainActivityRecyclerview?.adapter =
                            MainAdapter(dataModel) { data ->
                                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT)
                                    .show()
                            }
                    } else {
                        adapter?.setData(dataModel)
                    }
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (state.progress != null) {
                    binding?.progressBarHorizontal?.isVisible = true
                    binding?.progressBarRound?.isVisible = false
                    binding?.progressBarHorizontal?.progress = state.progress
                } else {
                    binding?.progressBarHorizontal?.isVisible = false
                    binding?.progressBarRound?.isVisible = true
                }
            }
            is AppState.Error -> showErrorScreen(state.error.message)
        }
    }

    private fun showErrorScreen(error: String?) {
        showViewError()
        binding?.errorTextview?.text = error ?: getString(R.string.undefined_error)
        binding?.reloadButton?.setOnClickListener {
            viewModel.getData(HI, true).observe(this) { renderData(it) }
        }
    }

    private fun showViewSuccess() {
        binding?.successLinearLayout?.isVisible = true
        binding?.loadingFrameLayout?.isVisible = false
        binding?.errorLinearLayout?.isVisible = false
    }

    private fun showViewLoading() {
        binding?.successLinearLayout?.isVisible = false
        binding?.loadingFrameLayout?.isVisible = true
        binding?.errorLinearLayout?.isVisible = false
    }

    private fun showViewError() {
        binding?.successLinearLayout?.isVisible = false
        binding?.loadingFrameLayout?.isVisible = false
        binding?.errorLinearLayout?.isVisible = true
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private companion object {
        const val HI = "hi"
    }
}