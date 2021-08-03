package com.katyrin.dictionaryapp.view.base


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.katyrin.dictionaryapp.R
import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.networkstatus.NetworkState
import com.katyrin.dictionaryapp.data.networkstatus.NetworkStateImpl
import com.katyrin.dictionaryapp.view.AlertDialogFragment
import com.katyrin.dictionaryapp.viewmodel.BaseViewModel
import com.katyrin.dictionaryapp.viewmodel.interactor.Interactor

abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity() {

    val networkState: NetworkState by lazy { NetworkStateImpl(applicationContext) }
    abstract val model: BaseViewModel<T>

    protected var isNetworkAvailable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNetworkAvailable = networkState.isOnline()
    }

    override fun onResume() {
        super.onResume()
        isNetworkAvailable = networkState.isOnline()
        if (!isNetworkAvailable && isDialogNull()) {
            showNoInternetConnectionDialog()
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

    private fun isDialogNull(): Boolean {
        return supportFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null
    }

    abstract fun renderData(dataModel: T)

    private companion object {
        const val DIALOG_FRAGMENT_TAG = "BaseActivity"
    }
}