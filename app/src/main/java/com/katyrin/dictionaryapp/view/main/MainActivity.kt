package com.katyrin.dictionaryapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.katyrin.dictionaryapp.R
import com.katyrin.dictionaryapp.data.interactor.MainInteractor
import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel
import com.katyrin.dictionaryapp.databinding.ActivityMainBinding
import com.katyrin.dictionaryapp.utils.convertMeaningsToString
import com.katyrin.core.view.BaseActivity
import com.katyrin.dictionaryapp.di.injectDependencies
import com.katyrin.dictionaryapp.view.description.DescriptionActivity
import com.katyrin.dictionaryapp.view.main.adapter.MainAdapter
import com.katyrin.dictionaryapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<AppState, MainInteractor>() {

    override lateinit var model: MainViewModel
    private lateinit var splitInstallManager: SplitInstallManager

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
                splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
                val request =
                    SplitInstallRequest
                        .newBuilder()
                        .addModule(HISTORY_ACTIVITY_FEATURE_NAME)
                        .build()

                splitInstallManager
                    .startInstall(request)
                    .addOnSuccessListener {
                        val intent = Intent().setClassName(packageName, HISTORY_ACTIVITY_PATH)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            applicationContext,
                            "Couldn't download feature: " + it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
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
        check(binding?.mainActivityRecyclerview?.adapter == null) { ADAPTER_NULL_TEXT }
        injectDependencies()
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

    private companion object {
        const val HISTORY_ACTIVITY_PATH = "com.katyrin.historyscreen.view.HistoryActivity"
        const val HISTORY_ACTIVITY_FEATURE_NAME = "historyScreen"
        const val ADAPTER_NULL_TEXT = "The ViewModel should be initialised first"
    }
}