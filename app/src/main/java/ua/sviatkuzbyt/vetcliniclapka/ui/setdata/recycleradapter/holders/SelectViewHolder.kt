package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders

import android.view.View
import android.widget.Button
import android.widget.TextView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem

class SelectViewHolder(view: View, private val action: Action) : SetRecordViewHolder(view) {

    interface Action{
        fun select(position: Int, item: SetRecordItem)
    }

    private val label = view.findViewById<TextView>(R.id.itemSelectLabel)
    private val button = view.findViewById<Button>(R.id.selectItemButton)

    override fun bind(item: SetRecordItem, position: Int){
        label.setText(item.label)

        if (item.labelData.isNotBlank())
            button.text = item.labelData

        val icon = when(item.apiName){
            "breed" -> R.drawable.ic_paw
            else -> R.drawable.ic_people_one
        }

        button.setCompoundDrawablesWithIntrinsicBounds(
            icon, 0, R.drawable.ic_next, 0
        )

        button.setOnClickListener {
            action.select(position, item)
        }
    }
}