package ua.sviatkuzbyt.vetcliniclapka.ui.elements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem

interface RecordAction{
    fun clickItem(id: Int)
}

class RecordAdapter(
    private val dataSet: MutableList<RecordItem>,
    private val action: RecordAction,
    private val iconRes: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val EMPTY_VIEW = 0
    private val RECORD_VIEW = 1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon = view.findViewById<View>(R.id.recordIcon)
        private val label = view.findViewById<TextView>(R.id.recordLabel)
        private val subtext = view.findViewById<TextView>(R.id.recordSubtext)
        private val root = view.rootView

        init { icon.setBackgroundResource(iconRes) }

        fun bind(item: RecordItem){
            label.text = item.label
            subtext.text = item.subtext

            root.setOnClickListener {
                action.clickItem(item.id)
            }
        }
    }

    inner class EmptyHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return if (viewType == RECORD_VIEW){
           ViewHolder(
               LayoutInflater.from(viewGroup.context)
               .inflate(R.layout.item_record, viewGroup, false)
           )
       } else{
           EmptyHolder(
               LayoutInflater.from(viewGroup.context)
               .inflate(R.layout.item_empty, viewGroup, false)
           )
       }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ViewHolder)
            viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = if (dataSet.isEmpty()) 1 else dataSet.size

    override fun getItemViewType(position: Int): Int {
        return if(dataSet.isEmpty()) EMPTY_VIEW
        else RECORD_VIEW
    }
}