package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordItem

class TextViewHolder(view: View) : SetRecordViewHolder(view) {
    private val label = view.findViewById<TextView>(R.id.setItemLabel)
    private val text = view.findViewById<EditText>(R.id.setItemText)

    override fun bind(item: SetRecordItem){
        label.setText(item.label)
        if(item.data.isNotBlank()) text.setText(item.data)

        text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                item.data = text.text.toString()
            }
        })
    }
}