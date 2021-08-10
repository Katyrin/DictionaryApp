package com.katyrin.core.view


import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.katyrin.core.R
import com.katyrin.core.databinding.LoadingLayoutBinding
import com.katyrin.core.interactor.Interactor
import com.katyrin.core.viewmodel.BaseViewModel
import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel
import com.katyrin.utils.network.NetworkState
import com.katyrin.utils.network.NetworkStateImpl
import com.katyrin.utils.view.AlertDialogFragment
import kotlinx.coroutines.*

abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity() {

    private var loadingBinding: LoadingLayoutBinding? = null
    abstract val model: BaseViewModel<T>
    protected val networkState: NetworkState by lazy { NetworkStateImpl(applicationContext) }

    protected val baseActivityCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
        })

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        loadingBinding = LoadingLayoutBinding.inflate(layoutInflater)
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
                    loadingBinding?.progressBarHorizontal?.isVisible = true
                    loadingBinding?.progressBarRound?.isVisible = false
                    loadingBinding?.progressBarHorizontal?.progress = appState.progress!!
                } else {
                    loadingBinding?.progressBarHorizontal?.isVisible = false
                    loadingBinding?.progressBarRound?.isVisible = true
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
        loadingBinding?.loadingFrameLayout?.isVisible = false
    }

    private fun showViewLoading() {
        loadingBinding?.loadingFrameLayout?.isVisible = true
    }

    private fun isDialogNull(): Boolean =
        supportFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null

    abstract fun setDataToAdapter(data: List<DataModel>)

    protected fun cancelJob() {
        baseActivityCoroutineScope.coroutineContext.cancelChildren()
    }

    override fun onDestroy() {
        loadingBinding = null
        cancelJob()
        super.onDestroy()
    }

    private companion object {
        const val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG"
    }
}