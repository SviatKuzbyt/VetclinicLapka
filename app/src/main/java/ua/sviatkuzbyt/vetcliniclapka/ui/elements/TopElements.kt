package ua.sviatkuzbyt.vetcliniclapka.ui.elements

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ConstState
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.records.RecordsActivity
import java.io.FileNotFoundException
import java.net.ConnectException
import java.net.UnknownHostException

fun hideKeyboard(view: View) {
    val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun makeToast(context: Context, text: Int){
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun postError(error: Exception, data: MutableLiveData<Int>){
    val message = when (error) {
        is NoTextException -> R.string.no_text
        is ConnectException -> R.string.no_connection
        is UnknownHostException -> R.string.no_connection
        is FileNotFoundException -> R.string.error_server
        is SavePdfException -> R.string.save_pdf_error
        else -> R.string.error
    }
    data.postValue(message)

    Log.e("sklt_error", "Error type: ${error::class.java.simpleName}", error)
}

class NoTextException: Exception()
class SavePdfException: Exception()

fun openSelectActivity(
    table: String,
    filter: String?,
    position: Int,
    activityResult: ActivityResultLauncher<Intent>,
    context: Context)
{
    val openIntent = Intent(context, RecordsActivity::class.java).apply {
        putExtra("label", context.getString(R.string.select_recor))
        putExtra("table", table)
        putExtra("openMode", ConstState.RECORD_ACTION_SELECT)
        filter?.let { putExtra("filter", it) }
        putExtra("forPosition", position)
    }

    activityResult.launch(openIntent)
}