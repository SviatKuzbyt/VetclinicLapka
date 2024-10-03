package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders

import android.view.View
import android.widget.Button
import android.widget.TextView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordItem

class SelectViewHolder(view: View) : SetRecordViewHolder(view) {
    private val label = view.findViewById<TextView>(R.id.itemSelectLabel)
    private val button = view.findViewById<Button>(R.id.selectItemButton)

    override fun bind(item: SetRecordItem){
        label.setText(item.label)

        val icon = when(item.apiName){
            "beard" -> R.drawable.ic_paw
            "owner" -> R.drawable.ic_people_one
            else -> R.drawable.ic_calendar_full
        }

        button.setCompoundDrawablesWithIntrinsicBounds(
            icon, 0, R.drawable.ic_next, 0
        )
    }
}