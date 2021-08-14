package com.katyrin.core.view


import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.katyrin.core.R
import com.katyrin.core.interactor.Interactor
import com.katyrin.core.viewmodel.BaseViewModel
import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel
import com.katyrin.utils.delegate.viewById
import com.katyrin.utils.extensions.toast
import com.katyrin.utils.network.NetworkState
import com.katyrin.utils.network.NetworkStateImpl
import com.katyrin.utils.view.AlertDialogFragment
import kotlinx.coroutines.*

abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity() {

    private val progressBarHorizontal by viewById<ProgressBar>(R.id.progress_bar_horizontal)
    private val progressBarRound by viewById<ProgressBar>(R.id.progress_bar_round)
    private val loadingFrameLayout by viewById<ConstraintLayout>(R.id.loading_frame_layout)

    abstract val model: BaseViewModel<T>
    protected val networkState: NetworkState by lazy { NetworkStateImpl(applicationContext) }

    protected val baseActivityCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            throwable.message?.let { toast(it) }
        })

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        checkNetworkState()
    }

    private fun checkNetworkState() {
        cancelJob()
        baseActivityCoroutineScope.launch {
            val isNetworkAvailable = networkState.isOnline()
            if (!isNetworkAvailable && isDialogNull()) showNoInternetConnectionDialog()
        }
    }

    protected fun renderData(appState: T) {
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                appState.data?.let {
                    if (it.isEmpty()) {
                        showAlertDialog(
                            getString(R.string.dialog_tittle_sorry),
                            getString(R.string.empty_server_response_on_success)
                        )
                    } else {
                        setDataToAdapter(it)
                    }
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    progressBarHorizontal.isVisible = true
                    progressBarRound.isVisible = false
                    progressBarHorizontal.progress = appState.progress!!
                } else {
                    progressBarHorizontal.isVisible = false
                    progressBarRound.isVisible = true
                }
            }
            is AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }

    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        AlertDialogFragment.newInstance(title, message)
            .show(supportFragmentManager, DIALOG_FRAGMENT_TAG)
    }

    private fun showViewWorking() {
        loadingFrameLayout.isVisible = false
    }

    private fun showViewLoading() {
        loadingFrameLayout.isVisible = true
    }

    private fun isDialogNull(): Boolean =
        supportFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null

    abstract fun setDataToAdapter(data: List<DataModel>)

    protected fun cancelJob() {
        baseActivityCoroutineScope.coroutineContext.cancelChildren()
    }

    override fun onDestroy() {
        cancelJob()
        super.onDestroy()
    }

    private companion object {
        const val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG"
    }
}