package ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.set

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem

abstract class SetRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: SetRecordItem)
}