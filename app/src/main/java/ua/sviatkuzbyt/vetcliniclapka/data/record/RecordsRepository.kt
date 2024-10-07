package ua.sviatkuzbyt.vetcliniclapka.data.record

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi

class RecordsRepository(private val table: String) {

    private val records = mutableListOf<RecordItem>()

    private val filterList = RecordsFilter().getFilterList(table)
    private var currentFilter = filterList[0].apiName

    private val icon = when(table){
        "pet" -> R.drawable.ic_round_pets
        "owner" -> R.drawable.ic_round_people
        "vet" -> R.drawable.ic_round_vet
        "breed" -> R.drawable.ic_round_paw
        else -> R.drawable.ic_round_record
    }

    fun getAllData(): MutableList<RecordItem>{
        val text = ServerApi.getData(table)
        updateList(Gson().fromJson(text, getType))
        return records
    }

    private fun updateList(list: MutableList<RecordItem>){
        records.clear()
        records.addAll(list)
    }

    fun getFilterData(filter: String): MutableList<RecordItem>{
        val trimFilter = filter.trim()
        val text = ServerApi.getData("$table/filter/$currentFilter/$trimFilter")
        updateList(Gson().fromJson(text, getType))
        return records
    }

    fun getFilterDataById(filter: String): MutableList<RecordItem>{
        Log.v("sklt", "$table/filter/id/$filter")
        val text = ServerApi.getData("$table/filter/id/$filter")
        Log.v("sklt", text)
        updateList(Gson().fromJson(text, getType))
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

    companion object{
        private val getType = object : TypeToken<MutableList<RecordItem>>() {}.type
    }
}

data class RecordItem(
    val id: Int,
    val label: String,
    val subtext: String
)