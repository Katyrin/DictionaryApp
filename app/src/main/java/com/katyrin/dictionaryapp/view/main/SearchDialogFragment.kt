package com.katyrin.dictionaryapp.view.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.katyrin.dictionaryapp.databinding.FragmentSearchDialogBinding

class SearchDialogFragment : BottomSheetDialogFragment() {

    interface OnSearchClickListener {
        fun onClick(searchWord: String)
    }

    private var binding: FragmentSearchDialogBinding? = null
    private var onSearchClickListener: OnSearchClickListener? = null

    private val textWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            binding?.searchButton?.isEnabled = binding?.searchEditText?.text != null &&
                    binding?.searchEditText?.text.toString().isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }

    private val onSearchButtonClickListener =
        View.OnClickListener {
            onSearchClickListener?.onClick(binding?.searchEditText?.text.toString())
            dismiss()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSearchDialogBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.searchButton?.setOnClickListener(onSearchButtonClickListener)
        binding?.searchEditText?.addTextChangedListener(textWatcher)
        addOnClearClickListener()
    }

    override fun onDestroyView() {
        onSearchClickListener = null
        binding = null
        super.onDestroyView()
    }

    private fun addOnClearClickListener() {
        binding?.searchInputLayout?.setEndIconOnClickListener {
            binding?.searchEditText?.setText("")
            binding?.searchButton?.isEnabled = false
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