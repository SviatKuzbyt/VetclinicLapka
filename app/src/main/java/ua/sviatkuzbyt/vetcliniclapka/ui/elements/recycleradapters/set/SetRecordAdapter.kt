package ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.set

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem

class SetRecordAdapter(
    private val dataSet: List<SetRecordItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val label = view.findViewById<TextView>(R.id.setItemLabel)
        private val text = view.findViewById<EditText>(R.id.setItemText)

        fun bind(item: SetRecordItem){
            label.setText(item.label)

            if(item.data.isNotBlank()) text.setText(item.data)

            text.setOnFocusChangeListener { _, b ->
                if (!b){
                    item.data = text.text.toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return TextViewHolder(
           LayoutInflater.from(viewGroup.context)
               .inflate(R.layout.item_set_record, viewGroup, false)
       )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when(viewHolder){
            is TextViewHolder -> viewHolder.bind(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size
}