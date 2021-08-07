package com.katyrin.dictionaryapp.view.description

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.katyrin.dictionaryapp.R
import com.katyrin.dictionaryapp.data.networkstatus.NetworkState
import com.katyrin.dictionaryapp.data.networkstatus.NetworkStateImpl
import com.katyrin.dictionaryapp.databinding.ActivityDescriptionBinding
import com.katyrin.dictionaryapp.view.AlertDialogFragment

class DescriptionActivity : AppCompatActivity() {

    private val networkState: NetworkState by lazy { NetworkStateImpl(applicationContext) }
    private var binding: ActivityDescriptionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setActionbarHomeButtonAsUp()
        binding?.descriptionScreenSwipeRefreshLayout?.setOnRefreshListener { startLoadingOrShowError() }
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
        binding?.descriptionHeader?.text = bundle?.getString(WORD_EXTRA)
        binding?.descriptionTextview?.text = bundle?.getString(DESCRIPTION_EXTRA)
        val imageLink = bundle?.getString(URL_EXTRA)
        if (imageLink.isNullOrBlank()) {
            stopRefreshAnimationIfNeeded()
        } else {
            binding?.descriptionImageview?.let { useGlideToLoadPhoto(it, imageLink) }
        }
    }

    private fun startLoadingOrShowError() {
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

    private fun stopRefreshAnimationIfNeeded() {
        if (binding?.descriptionScreenSwipeRefreshLayout?.isRefreshing == true)
            binding?.descriptionScreenSwipeRefreshLayout?.isRefreshing = false
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