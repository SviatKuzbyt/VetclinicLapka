package ua.sviatkuzbyt.vetcliniclapka.ui.recyclers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import ua.sviatkuzbyt.vetcliniclapka.R

class ReportAdapter(private val context: Context, private val items: MutableList<String>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): String = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_report, parent, false
        )

        val itemReportLabel: TextView = view.findViewById(R.id.itemReportLabel)
        val itemReportContent: TextView = view.findViewById(R.id.itemReportContent)

        if (position == 0){
            val label: View = view.findViewById(R.id.reportLabel)
            label.visibility = View.VISIBLE
            itemReportLabel.setText(R.string.report)
        } else{
            itemReportLabel.text = "${context.getString(R.string.num)}$position"
        }
        itemReportContent.text = getItem(position)
        return view
    }
}
