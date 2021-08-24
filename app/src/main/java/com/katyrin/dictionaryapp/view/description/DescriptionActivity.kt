package com.katyrin.dictionaryapp.view.description

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.katyrin.dictionaryapp.R
import com.katyrin.utils.delegate.viewById
import com.katyrin.utils.network.NetworkState
import com.katyrin.utils.network.NetworkStateImpl
import com.katyrin.utils.view.AlertDialogFragment
import kotlinx.coroutines.*

class DescriptionActivity : AppCompatActivity() {

    private val descriptionHeader by viewById<TextView>(R.id.description_header)
    private val descriptionTextview by viewById<TextView>(R.id.description_textview)
    private val descriptionImageview by viewById<ImageView>(R.id.description_imageview)
    private val descriptionScreenSwipeRefreshLayout by
    viewById<SwipeRefreshLayout>(R.id.description_screen_swipe_refresh_layout)

    private val networkState: NetworkState by lazy { NetworkStateImpl(applicationContext) }
    private val descriptionActivityCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        setActionbarHomeButtonAsUp()
        descriptionScreenSwipeRefreshLayout.setOnRefreshListener { startLoadingOrShowError() }
        setData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setActionbarHomeButtonAsUp() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setData() {
        val bundle = intent.extras
        descriptionHeader.text = bundle?.getString(WORD_EXTRA)
        descriptionTextview.text = bundle?.getString(DESCRIPTION_EXTRA)
        val imageLink = bundle?.getString(URL_EXTRA)
        if (imageLink.isNullOrBlank()) stopRefreshAnimationIfNeeded()
        else useGlideToLoadPhoto(descriptionImageview, imageLink)
    }

    private fun startLoadingOrShowError() {
        descriptionActivityCoroutineScope.launch {
            if (networkState.isOnline()) {
                setData()
            } else {
                AlertDialogFragment.newInstance(
                    getString(R.string.dialog_title_device_is_offline),
                    getString(R.string.dialog_message_device_is_offline)
                ).show(supportFragmentManager, DIALOG_FRAGMENT_TAG)
                stopRefreshAnimationIfNeeded()
            }
        }
    }

    private fun stopRefreshAnimationIfNeeded() {
        if (descriptionScreenSwipeRefreshLayout.isRefreshing)
            descriptionScreenSwipeRefreshLayout.isRefreshing = false
    }

    private fun useGlideToLoadPhoto(imageView: ImageView, imageLink: String) {
        Glide.with(imageView)
            .load("https:$imageLink")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    stopRefreshAnimationIfNeeded()
                    imageView.setImageResource(R.drawable.ic_load_error_vector)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    stopRefreshAnimationIfNeeded()
                    return false
                }
            })
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_no_photo_vector)
                    .centerCrop()
            )
            .into(imageView)
    }

    private fun cancelJob() {
        descriptionActivityCoroutineScope.coroutineContext.cancelChildren()
    }

    override fun onDestroy() {
        cancelJob()
        super.onDestroy()
    }

    companion object {
        private const val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG"
        private const val WORD_EXTRA = "WORD_EXTRA"
        private const val DESCRIPTION_EXTRA = "DESCRIPTION_EXTRA"
        private const val URL_EXTRA = "URL_EXTRA"

        fun getIntent(context: Context, word: String, description: String, url: String?): Intent =
            Intent(context, DescriptionActivity::class.java).apply {
                putExtra(WORD_EXTRA, word)
                putExtra(DESCRIPTION_EXTRA, description)
                putExtra(URL_EXTRA, url)
            }
    }
}