package ua.sviatkuzbyt.vetcliniclapka.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ua.sviatkuzbyt.vetcliniclapka.R
import java.net.URL

class RecordsRepository(private val table: String) {

    private val records = mutableListOf<RecordItem>()
    private val filterList = RecordsFilter().getFilterList(table)
    private var currentFilter = filterList[0].apiName

    private val icon = when(table){
        "pet" -> R.drawable.ic_round_pets
        "owner" -> R.drawable.ic_round_people
        "vet" -> R.drawable.ic_round_vet
        else -> R.drawable.ic_round_record
    }

    fun getAllData(): MutableList<RecordItem>{
        val text = URL("http://sviat-fedora.local:3000/$table").readText()
        val gson = Gson()
        updateList(gson.fromJson(text, getType))
        return records
    }

    fun getFilterData(filter: String): MutableList<RecordItem>{
        val trimFilter = filter.trim()
        val link = "http://sviat-fedora.local:3000/$table/filter/$currentFilter/$trimFilter"
        val text = URL(link).readText()
        val gson = Gson()
        updateList(gson.fromJson(text, getType))
        return records
    }

    private fun updateList(list: MutableList<RecordItem>){
        records.clear()
        records.addAll(list)
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