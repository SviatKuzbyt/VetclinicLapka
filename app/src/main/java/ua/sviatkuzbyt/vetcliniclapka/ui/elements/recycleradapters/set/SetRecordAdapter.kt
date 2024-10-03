package ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.set

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordRepository

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

           else ->
               TextViewHolder(
                   LayoutInflater.from(viewGroup.context)
                       .inflate(R.layout.item_set_record, viewGroup, false)
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