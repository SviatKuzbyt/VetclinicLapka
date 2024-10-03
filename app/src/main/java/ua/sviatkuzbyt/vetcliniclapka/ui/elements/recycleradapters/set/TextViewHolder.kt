package ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.set

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem

class TextViewHolder(view: View) : SetRecordViewHolder(view) {
    private val label = view.findViewById<TextView>(R.id.setItemLabel)
    private val text = view.findViewById<EditText>(R.id.setItemText)

    override fun bind(item: SetRecordItem){
        label.setText(item.label)
        if(item.data.isNotBlank()) text.setText(item.data)

        text.setOnFocusChangeListener { _, b ->
            if (!b) item.data = text.text.toString()
        }
    }
}