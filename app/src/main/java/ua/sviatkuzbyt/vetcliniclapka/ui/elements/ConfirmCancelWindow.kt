package ua.sviatkuzbyt.vetcliniclapka.ui.elements

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R

class ConfirmCancelWindow {
    private lateinit var dialog: AlertDialog
    private var builder: AlertDialog.Builder

    constructor(fragment: BottomSheetDialogFragment) {
        builder = AlertDialog.Builder(fragment.requireContext(), R.style.ConfirmCancelWindow)
        initialize(fragment)
    }

    constructor(activity: AppCompatActivity) {
        builder = AlertDialog.Builder(activity, R.style.ConfirmCancelWindow)
        initialize(activity)
    }

    private fun initialize(fragment: BottomSheetDialogFragment) {
        builder
            .setTitle(R.string.cancel_changes)
            .setPositiveButton(R.string.yes) { _, _ ->
                fragment.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }

        dialog = builder.create()
    }

    private fun initialize(activity: AppCompatActivity) {
        builder
            .setTitle(R.string.cancel_changes)
            .setPositiveButton(R.string.yes) { _, _ ->
                activity.finish()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }

        dialog = builder.create()
    }

    fun showWindow() {
        dialog.show()
    }
}