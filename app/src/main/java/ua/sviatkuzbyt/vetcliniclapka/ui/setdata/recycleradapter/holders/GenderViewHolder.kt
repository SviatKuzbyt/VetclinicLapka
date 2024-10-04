package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders

import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.ui.records.fragments.CalendarFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.SetRecordAdapter

class GenderViewHolder(view: View) : SetRecordViewHolder(view) {

    private val label = view.findViewById<TextView>(R.id.itemGenderLabel)
    private val radio = view.findViewById<RadioGroup>(R.id.itemRadioGroup)

    override fun bind(item: SetRecordItem, position: Int){
        label.setText(item.label)

        val selectId =
            if (item.data == "1") R.id.itemRadioMale
            else R.id.itemRadioFemale

        radio.check(selectId)

        radio.setOnCheckedChangeListener { _, i ->
            item.data =
                if (i == R.id.itemRadioMale) "1"
                else "2"
        }
    }
}