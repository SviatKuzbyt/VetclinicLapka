package ua.sviatkuzbyt.vetcliniclapka.ui.elements.view

import android.content.Context
import android.widget.Toast

fun makeErrorToast(text: Pair<Int, String?>, context: Context){
    val message = context.getString(text.first)
    val details = if(text.second != null) ": ${text.second}" else ""

    Toast.makeText(context, "$message$details", Toast.LENGTH_LONG).show()
}