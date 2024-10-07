package ua.sviatkuzbyt.vetcliniclapka.ui.info.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.info.InfoSharedData
import ua.sviatkuzbyt.vetcliniclapka.data.info.InfoText

class SharedDataAdapter(
    private val dataSet: List<InfoSharedData>,
    private val action: Action
) : RecyclerView.Adapter<SharedDataAdapter.SharedDataViewHolder>() {

    interface Action{
        fun openRecordActivity(tableFilter: String)
    }

    inner class SharedDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon = view.findViewById<View>(R.id.openItemIcon)
        private val content = view.findViewById<TextView>(R.id.openItemText)
        private val root = view.rootView

        fun bind(item: InfoSharedData){
            icon.setBackgroundResource(item.icon)
            content.setText(item.label)

            root.setOnClickListener {
                action.openRecordActivity(item.filterTable)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SharedDataViewHolder {
       return SharedDataViewHolder(
           LayoutInflater.from(viewGroup.context)
               .inflate(R.layout.item_frame_open, viewGroup, false)
       )
    }

    override fun onBindViewHolder(viewHolder: SharedDataViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}