package ua.sviatkuzbyt.vetcliniclapka.ui.elements

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import ua.sviatkuzbyt.vetcliniclapka.R
import java.io.FileNotFoundException
import java.io.Serializable
import java.net.ConnectException
import java.net.UnknownHostException

fun hideKeyboard(view: View) {
    val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun makeToast(context: Context, text: Int){
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun makeToast(context: Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun postError(error: Exception, data: MutableLiveData<Int>){
    val message = when (error) {
        is NoTextException -> R.string.no_text
        is ConnectException -> R.string.no_connection
        is UnknownHostException -> R.string.no_connection
        is FileNotFoundException -> R.string.error_server
        else -> R.string.error
    }
    data.postValue(message)

    //TEMP (for test)
    Log.e("sklt_error", "Error type: ${error::class.java.simpleName}", error)
}

class NoTextException : Exception(), Serializable