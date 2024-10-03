package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders.CheckboxSpecViewHolder
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders.GenderViewHolder
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders.SelectViewHolder
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders.SetRecordViewHolder
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders.TextViewHolder

class SetRecordAdapter(
    private val dataSet: List<SetRecordItem>
) : RecyclerView.Adapter<SetRecordViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SetRecordViewHolder {
       return when(viewType){
           SetRecordRepository.TYPE_CHECKBOX_SPEC ->
               CheckboxSpecViewHolder(
                   LayoutInflater.from(viewGroup.context)
                       .inflate(R.layout.item_set_vet_spec, viewGroup, false)
               )

           SetRecordRepository.TYPE_TEXT ->
               TextViewHolder(
                   LayoutInflater.from(viewGroup.context)
                       .inflate(R.layout.item_set_record, viewGroup, false)
               )

           SetRecordRepository.TYPE_RADIO ->
               GenderViewHolder(
                   LayoutInflater.from(viewGroup.context)
                       .inflate(R.layout.item_select_gender, viewGroup, false)
               )

           else ->
               SelectViewHolder(
                   LayoutInflater.from(viewGroup.context)
                       .inflate(R.layout.item_select, viewGroup, false)
               )
       }
    }

    override fun onBindViewHolder(viewHolder: SetRecordViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    override fun getItemViewType(position: Int): Int {
        return dataSet[position].type
    }
}