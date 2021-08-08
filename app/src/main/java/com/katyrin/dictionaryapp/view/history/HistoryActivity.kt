package com.katyrin.dictionaryapp.view.history

import android.os.Bundle
import com.katyrin.dictionaryapp.data.interactor.HistoryInteractor
import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.databinding.ActivityHistoryBinding
import com.katyrin.dictionaryapp.view.base.BaseActivity
import com.katyrin.dictionaryapp.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : BaseActivity<AppState, HistoryInteractor>() {

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

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    private fun iniViewModel() {
        if (binding?.historyActivityRecyclerview?.adapter != null)
            throw IllegalStateException("The ViewModel should be initialised first")
        val viewModel: HistoryViewModel by viewModel()
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
}