package com.katyrin.dictionaryapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.katyrin.core.view.BaseActivity
import com.katyrin.dictionaryapp.R
import com.katyrin.dictionaryapp.data.interactor.MainInteractor
import com.katyrin.dictionaryapp.databinding.ActivityMainBinding
import com.katyrin.dictionaryapp.di.injectDependencies
import com.katyrin.dictionaryapp.utils.convertMeaningsToString
import com.katyrin.dictionaryapp.view.description.DescriptionActivity
import com.katyrin.dictionaryapp.view.main.adapter.MainAdapter
import com.katyrin.dictionaryapp.viewmodel.MainViewModel
import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<AppState, MainInteractor>() {

    override lateinit var model: MainViewModel
    private lateinit var splitInstallManager: SplitInstallManager
    private lateinit var appUpdateManager: AppUpdateManager

    private val stateUpdatedListener: InstallStateUpdatedListener =
        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) popupSnackbarForCompleteUpdate()
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                appUpdateManager.unregisterListener(stateUpdatedListener)
            else
                Toast.makeText(
                    applicationContext,
                    "$REPEAT_UPDATE_TEXT $resultCode",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        iniViewModel()
        initViews()
        checkForUpdates()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED)
                    popupSnackbarForCompleteUpdate()
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) startUpdate(appUpdateInfo)
            }
    }

    private fun startUpdate(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            IMMEDIATE,
            this,
            REQUEST_CODE
        )
    }

    private fun checkForUpdates() {
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfo = appUpdateManager.appUpdateInfo
        appUpdateInfo.addOnSuccessListener { appUpdateIntent ->
            if (appUpdateIntent.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateIntent.isUpdateTypeAllowed(IMMEDIATE)
            ) {
                appUpdateManager.registerListener(stateUpdatedListener)
                startUpdate(appUpdateIntent)
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        binding?.root?.let { view ->
            Snackbar.make(
                view,
                getString(R.string.update_has_downloaded_text),
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setAction(getString(R.string.restart)) { appUpdateManager.completeUpdate() }
                show()
            }
        }
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
        const val REQUEST_CODE = 54
        const val HISTORY_ACTIVITY_PATH = "com.katyrin.historyscreen.view.HistoryActivity"
        const val HISTORY_ACTIVITY_FEATURE_NAME = "historyScreen"
        const val ADAPTER_NULL_TEXT = "The ViewModel should be initialised first"
        const val REPEAT_UPDATE_TEXT = "Update flow failed! Result code:"
    }
}