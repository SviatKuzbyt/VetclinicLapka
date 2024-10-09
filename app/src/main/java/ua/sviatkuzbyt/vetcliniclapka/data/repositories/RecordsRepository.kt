package ua.sviatkuzbyt.vetcliniclapka.data.repositories

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.FilterItem
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.data.prelists.RecordsFilter

class RecordsRepository(private val table: String) {

    private val records = mutableListOf<RecordItem>()
    private val filterList = RecordsFilter().getFilterList(table)
    private var currentFilter = filterList[0].apiName
    private var lastFilter = table

    private val icon = when(table){
        "pet" -> R.drawable.ic_round_pets
        "owner" -> R.drawable.ic_round_people
        "vet" -> R.drawable.ic_round_vet
        "breed" -> R.drawable.ic_round_paw
        else -> R.drawable.ic_round_record
    }

    fun getAllData(): MutableList<RecordItem>{
        val text = ServerApi.getData(table)
        updateList(Gson().fromJson(text, ServerApi.getListRecordItemType))
        return records
    }

    private fun updateList(list: MutableList<RecordItem>){
        records.clear()
        records.addAll(list)
    }

    fun getFilterData(filter: String): MutableList<RecordItem>{
        val trimFilter = filter.trim()
        lastFilter = "$table/filter/$currentFilter/$trimFilter"
        val text = ServerApi.getData(lastFilter)
        updateList(Gson().fromJson(text, ServerApi.getListRecordItemType))
        return records
    }

    fun getStartFilterData(filter: String): MutableList<RecordItem>{
        val text = ServerApi.getData("$table/filter/$filter")
        updateList(Gson().fromJson(text, ServerApi.getListRecordItemType))
        return records
    }

    fun getIcon() = icon

    fun getFilterList(): List<FilterItem> {
        return filterList
    }

    fun updateFilterList(oldPosition: Int, newPosition: Int) {
        filterList[oldPosition].isSelected = false
        filterList[newPosition].isSelected = true
        currentFilter = filterList[newPosition].apiName
    }

    fun isSelectedDate() = currentFilter == "date"

    fun reload(): MutableList<RecordItem> {
        val text = ServerApi.getData(lastFilter)
        updateList(Gson().fromJson(text, ServerApi.getListRecordItemType))
        return records
    }

}

