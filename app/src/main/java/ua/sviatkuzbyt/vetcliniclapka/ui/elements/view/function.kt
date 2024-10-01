package ua.sviatkuzbyt.vetcliniclapka.ui.elements.view

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun makeErrorToast(text: Pair<Int, String?>, context: Context){
    val message = context.getString(text.first)
    val details = if(text.second != null) ": ${text.second}" else ""

    Toast.makeText(context, "$message$details", Toast.LENGTH_LONG).show()
    Log.e("sklt_error", text.second?: "-_-")
}

fun hideKeyboard(view: View) {
    val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}