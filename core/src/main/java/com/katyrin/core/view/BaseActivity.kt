package com.katyrin.core.view


import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.katyrin.core.R
import com.katyrin.core.interactor.Interactor
import com.katyrin.core.viewmodel.BaseViewModel
import com.katyrin.model.data.AppState
import com.katyrin.model.data.userdata.DataModel
import com.katyrin.utils.delegate.viewById
import com.katyrin.utils.extensions.toast
import com.katyrin.utils.network.NetworkState
import com.katyrin.utils.view.AlertDialogFragment
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity() {

    private val progressBarHorizontal by viewById<ProgressBar>(R.id.progress_bar_horizontal)
    private val progressBarRound by viewById<ProgressBar>(R.id.progress_bar_round)
    private val loadingFrameLayout by viewById<ConstraintLayout>(R.id.loading_frame_layout)
    protected var isNetworkAvailable: Boolean = true

    abstract val model: BaseViewModel<T>
    protected val networkState: NetworkState by inject()
    protected abstract val layoutRes: Int

    protected val baseActivityCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> toast(throwable.message) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        checkNetworkState()
    }

    private fun checkNetworkState() {
        cancelJob()
        baseActivityCoroutineScope.launch {
            isNetworkAvailable = networkState.isOnline()
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

    protected fun showNoInternetConnectionDialog(): Unit =
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )

    protected fun showAlertDialog(title: String?, message: String?): Unit =
        AlertDialogFragment.newInstance(title, message)
            .show(supportFragmentManager, DIALOG_FRAGMENT_TAG)

    private fun showViewWorking() {
        loadingFrameLayout.isVisible = false
    }

    private fun showViewLoading() {
        loadingFrameLayout.isVisible = true
    }

    private fun isDialogNull(): Boolean =
        supportFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null

    abstract fun setDataToAdapter(data: List<DataModel>)

    protected fun cancelJob(): Unit = baseActivityCoroutineScope.coroutineContext.cancelChildren()

    override fun onDestroy() {
        cancelJob()
        super.onDestroy()
    }

    private companion object {
        const val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG"
    }
}