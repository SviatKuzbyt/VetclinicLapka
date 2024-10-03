package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordItem

abstract class SetRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: SetRecordItem)
}