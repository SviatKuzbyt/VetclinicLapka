package ua.sviatkuzbyt.vetcliniclapka.ui.info.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.InfoText

class TextAdapter(
    private val dataSet: List<InfoText>,
) : RecyclerView.Adapter<TextAdapter.TextViewHolder>() {

    inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val label = view.findViewById<TextView>(R.id.itemTextLabel)
        private val content = view.findViewById<TextView>(R.id.itemTextContent)

        fun bind(item: InfoText){
            label.setText(item.label)
            content.text = item.content
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TextViewHolder {
       return TextViewHolder(
           LayoutInflater.from(viewGroup.context)
               .inflate(R.layout.item_text, viewGroup, false)
       )
    }

    override fun onBindViewHolder(viewHolder: TextViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}