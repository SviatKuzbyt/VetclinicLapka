package ua.sviatkuzbyt.vetcliniclapka.ui.recyclers.setdata.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem

abstract class SetRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: SetRecordItem, position: Int = 0)
}