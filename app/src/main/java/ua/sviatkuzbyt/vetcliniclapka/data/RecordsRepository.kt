package ua.sviatkuzbyt.vetcliniclapka.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ua.sviatkuzbyt.vetcliniclapka.R
import java.net.URL

class RecordsRepository(private val table: String) {

    private var records = mutableListOf<RecordItem>()
    //temp
    private val filterList = listOf(
        FilterItem(R.string.pets, "pet", true),
        FilterItem(R.string.vets, "vet", false),
        FilterItem(R.string.appointment_history, "appointment", false)
    )
    private var currentFilter = ""

    private val icon = when(table){
        "pet" -> R.drawable.ic_round_pets
        "owner" -> R.drawable.ic_round_people
        "vet" -> R.drawable.ic_round_vet
        else -> R.drawable.ic_round_record
    }

    fun getAllData(): MutableList<RecordItem>{
        val text = URL("http://sviat-fedora.local:3000/$table").readText()
        val gson = Gson()
        records = gson.fromJson(text, getType)
        return records
    }

    fun getFilterData(filter: String, value: String): MutableList<RecordItem>{
        return mutableListOf()
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

    companion object{
        private val getType = object : TypeToken<MutableList<RecordItem>>() {}.type
    }
}

data class RecordItem(
    val id: Int,
    val label: String,
    val subtext: String
)

data class FilterItem(
    val label: Int,
    val apiName: String,
    var isSelected: Boolean
)