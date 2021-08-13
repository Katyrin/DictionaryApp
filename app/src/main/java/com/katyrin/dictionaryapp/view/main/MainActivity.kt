package com.katyrin.dictionaryapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.katyrin.dictionaryapp.R
import com.katyrin.dictionaryapp.data.interactor.MainInteractor
import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel
import com.katyrin.dictionaryapp.databinding.ActivityMainBinding
import com.katyrin.dictionaryapp.utils.convertMeaningsToString
import com.katyrin.core.view.BaseActivity
import com.katyrin.dictionaryapp.view.description.DescriptionActivity
import com.katyrin.historyscreen.view.HistoryActivity
import com.katyrin.dictionaryapp.view.main.adapter.MainAdapter
import com.katyrin.dictionaryapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<AppState, MainInteractor>() {

    override lateinit var model: MainViewModel

    private var binding: ActivityMainBinding? = null
    private val adapter: MainAdapter by lazy {
        MainAdapter { data ->
            startActivity(
                DescriptionActivity.getIntent(
                    this@MainActivity,
                    data.text!!,
                    convertMeaningsToString(data.meanings!!),
                    data.meanings!![0].imageUrl
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        iniViewModel()
        initViews()
    }

    private fun initViews() {
        binding?.mainActivityRecyclerview?.layoutManager = LinearLayoutManager(applicationContext)
        binding?.mainActivityRecyclerview?.adapter = adapter
        binding?.searchFab?.setOnClickListener {
            SearchDialogFragment.newInstance(supportFragmentManager).setOnSearchClickListener(
                object : SearchDialogFragment.OnSearchClickListener {
                    override fun onClick(searchWord: String) {
                        setClickFab(searchWord)
                    }
                }
            )
        }
    }

    private fun setClickFab(searchWord: String) {
        cancelJob()
        baseActivityCoroutineScope.launch {
            val isNetworkAvailable = networkState.isOnline()
            if (isNetworkAvailable) model.getData(searchWord, isNetworkAvailable)
            else showNoInternetConnectionDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            R.id.menu_search_history -> {
                openHistorySearchDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun openHistorySearchDialog() {
        SearchDialogFragment.newInstance(supportFragmentManager).setOnSearchClickListener(
            object : SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    model.searchHistoryByWord(searchWord)
                }
            }
        )
    }

    private fun iniViewModel() {
        if (binding?.mainActivityRecyclerview?.adapter != null)
            throw IllegalStateException("The ViewModel should be initialised first")
        val viewModel: MainViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(this) { renderData(it) }
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}