package ua.sviatkuzbyt.vetcliniclapka.ui.elements.view

import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R

class ConfirmCancelWindow(fragment: BottomSheetDialogFragment) {
    private val builder: AlertDialog.Builder = AlertDialog.Builder(fragment.requireContext(), R.style.ConfirmCancelWindow)
    private var dialog: AlertDialog

    init {
        builder
            .setTitle(R.string.cancel_changes)
            .setPositiveButton(R.string.yes) { _, _ ->
                fragment.dismiss()
            }
            .setNegativeButton(R.string.no) {dialog, _ ->
                dialog.cancel()
            }

        dialog = builder.create()
    }

    fun showWindow(){ dialog.show() }
}