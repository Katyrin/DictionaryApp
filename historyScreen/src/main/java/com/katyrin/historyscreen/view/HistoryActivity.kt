package com.katyrin.historyscreen.view

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.core.view.BaseActivity
import com.katyrin.historyscreen.R
import com.katyrin.historyscreen.di.injectDependencies
import com.katyrin.historyscreen.interactor.HistoryInteractor
import com.katyrin.historyscreen.viewmodel.HistoryViewModel
import com.katyrin.model.data.AppState
import com.katyrin.model.data.userdata.DataModel
import com.katyrin.utils.delegate.viewById
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope
import org.koin.core.scope.inject

class HistoryActivity : BaseActivity<AppState, HistoryInteractor>(), KoinScopeComponent {

    private val historyActivityRecyclerview by
    viewById<RecyclerView>(R.id.history_activity_recyclerview)
    override val scope: Scope by lazy { activityScope() }
    override lateinit var model: HistoryViewModel
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        iniViewModel()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        model.getData("", false)
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    private fun iniViewModel() {
        check(historyActivityRecyclerview.adapter == null) { ADAPTER_NULL_TEXT }
        injectDependencies()
        val viewModel: HistoryViewModel by inject()
        model = viewModel
        model.subscribe().observe(this@HistoryActivity) { renderData(it) }
    }

    private fun initViews() {
        historyActivityRecyclerview.adapter = adapter
    }

    private companion object {
        const val ADAPTER_NULL_TEXT = "The ViewModel should be initialised first"
    }
}