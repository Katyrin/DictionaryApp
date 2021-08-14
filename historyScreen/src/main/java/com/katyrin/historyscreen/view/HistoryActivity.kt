package com.katyrin.historyscreen.view

import android.os.Bundle
import com.katyrin.core.view.BaseActivity
import com.katyrin.historyscreen.databinding.ActivityHistoryBinding
import com.katyrin.historyscreen.di.injectDependencies
import com.katyrin.historyscreen.interactor.HistoryInteractor
import com.katyrin.historyscreen.viewmodel.HistoryViewModel
import com.katyrin.model.data.AppState
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope
import org.koin.core.scope.inject

class HistoryActivity : BaseActivity<AppState, HistoryInteractor>(), KoinScopeComponent {

    override val scope: Scope by lazy { activityScope() }
    private var binding: ActivityHistoryBinding? = null
    override lateinit var model: HistoryViewModel
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        iniViewModel()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        model.getData("", false)
    }

    override fun setDataToAdapter(data: List<com.katyrin.model.data.DataModel>) {
        adapter.setData(data)
    }

    private fun iniViewModel() {
        check(binding?.historyActivityRecyclerview?.adapter == null) { ADAPTER_NULL_TEXT }
        injectDependencies()
        val viewModel: HistoryViewModel by inject()
        model = viewModel
        model.subscribe().observe(this@HistoryActivity) { renderData(it) }
    }

    private fun initViews() {
        binding?.historyActivityRecyclerview?.adapter = adapter
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private companion object {
        const val ADAPTER_NULL_TEXT = "The ViewModel should be initialised first"
    }
}