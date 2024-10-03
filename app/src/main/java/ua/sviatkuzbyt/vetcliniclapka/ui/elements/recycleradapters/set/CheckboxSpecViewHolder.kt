package ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.set

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordRepository

class CheckboxSpecViewHolder(view: View) : SetRecordViewHolder(view) {
    private val label = view.findViewById<TextView>(R.id.setSpecLabel)
    private val cbDogs = view.findViewById<CheckBox>(R.id.checkBoxDogs)
    private val cbRodent = view.findViewById<CheckBox>(R.id.checkBoxRodent)
    private val cbBirds = view.findViewById<CheckBox>(R.id.checkBoxBirds)
    private val cbCats = view.findViewById<CheckBox>(R.id.checkBoxCats)

    private val checkBoxes = listOf(cbDogs, cbCats, cbBirds, cbRodent)

    override fun bind(item: SetRecordItem){
        label.setText(item.label)
         val dataChar = item.data.toCharArray()

        for (i in dataChar.indices){
            if (dataChar[i] == '1') checkBoxes[i].isChecked = true
            checkBoxes[i].setOnCheckedChangeListener { _, checked ->
                dataChar[i] = if (checked) '1'
                else '0'
                item.data = String(dataChar)
            }
        }
    }
}