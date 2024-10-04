package ua.sviatkuzbyt.vetcliniclapka.ui.records.recycleradapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.record.FilterItem

class FilterAdapter(
    private val dataSet: List<FilterItem>,
    private val action: Action
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    interface Action{
        fun selectedItem(oldPosition: Int, newPosition: Int)
    }

    private var oldSelectedPosition = 0

    inner class FilterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val button = view.findViewById<RadioButton>(R.id.filterRadioItem)

        fun bind(item: FilterItem, position: Int){
            button.setText(item.label)

            if (item.isSelected){
                oldSelectedPosition = position
                button.isChecked = true
            }

            button.setOnClickListener {
                action.selectedItem(oldSelectedPosition, position)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FilterViewHolder {
       return FilterViewHolder(
           LayoutInflater.from(viewGroup.context)
               .inflate(R.layout.item_filter, viewGroup, false)
       )
    }

    override fun onBindViewHolder(viewHolder: FilterViewHolder, position: Int) {
            viewHolder.bind(dataSet[position], position)
    }

    override fun getItemCount() = dataSet.size
}