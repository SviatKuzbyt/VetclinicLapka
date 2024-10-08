package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordItem

class CheckboxSpecViewHolder(view: View) : SetRecordViewHolder(view) {
    private val label = view.findViewById<TextView>(R.id.setSpecLabel)

    private val checkBoxes = listOf<CheckBox>(
        view.findViewById(R.id.checkBoxDogs),
        view.findViewById(R.id.checkBoxCats),
        view.findViewById(R.id.checkBoxBirds),
        view.findViewById(R.id.checkBoxRodent)
    )

    override fun bind(item: SetRecordItem, position: Int){
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