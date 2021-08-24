package com.katyrin.dictionaryapp.view.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.katyrin.dictionaryapp.R
import com.katyrin.utils.delegate.viewById

class SearchDialogFragment : BottomSheetDialogFragment() {

    interface OnSearchClickListener {
        fun onClick(searchWord: String)
    }

    private val searchButton by viewById<MaterialButton>(R.id.search_button)
    private val searchEditText by viewById<TextInputEditText>(R.id.search_edit_text)
    private val searchInputLayout by viewById<TextInputLayout>(R.id.search_input_layout)
    private var onSearchClickListener: OnSearchClickListener? = null

    private val textWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            searchButton.isEnabled = searchEditText.text != null &&
                    searchEditText.text.toString().isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }

    private val onSearchButtonClickListener =
        View.OnClickListener {
            onSearchClickListener?.onClick(searchEditText.text.toString())
            dismiss()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_search_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchButton.setOnClickListener(onSearchButtonClickListener)
        searchEditText.addTextChangedListener(textWatcher)
        addOnClearClickListener()
    }

    override fun onDestroyView() {
        onSearchClickListener = null
        super.onDestroyView()
    }

    private fun addOnClearClickListener() {
        searchInputLayout.setEndIconOnClickListener {
            searchEditText.setText("")
            searchButton.isEnabled = false
        }
    }

    internal fun setOnSearchClickListener(listener: OnSearchClickListener) {
        onSearchClickListener = listener
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "BOTTOM_SHEET_FRAGMENT_DIALOG_TAG"

        fun newInstance(fragmentManager: FragmentManager) = SearchDialogFragment().apply {
            show(fragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }
}