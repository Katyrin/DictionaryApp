package com.katyrin.utils.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.katyrin.utils.R

class AlertDialogFragment : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alertDialog = getStubAlertDialog(requireContext())
        val args = arguments
        if (args != null) {
            val title = args.getString(TITLE_EXTRA)
            val message = args.getString(MESSAGE_EXTRA)
            alertDialog = getAlertDialog(requireContext(), title, message)
        }
        return alertDialog
    }

    private fun getStubAlertDialog(context: Context): AlertDialog =
        getAlertDialog(context, null, null)

    private fun getAlertDialog(context: Context, title: String?, message: String?): AlertDialog {
        val builder = AlertDialog.Builder(context)
        var finalTitle: String? = context.getString(R.string.dialog_title_stub)
        if (!title.isNullOrBlank()) finalTitle = title
        builder.setTitle(finalTitle)
        if (!message.isNullOrBlank()) builder.setMessage(message)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.dialog_button_cancel) { dialog, _ -> dialog.dismiss() }
        return builder.create()
    }

    companion object {
        private const val TITLE_EXTRA = "TITLE_EXTRA"
        private const val MESSAGE_EXTRA = "MESSAGE_EXTRA"

        fun newInstance(title: String?, message: String?): AlertDialogFragment =
            AlertDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE_EXTRA, title)
                    putString(MESSAGE_EXTRA, message)
                }
            }
    }
}