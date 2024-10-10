package ua.sviatkuzbyt.vetcliniclapka.data.repositories

import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.data.prelists.RecordsFilter

class RecordsRepository(private val table: String) {
    private val records = mutableListOf<RecordItem>()
    private val filterList = RecordsFilter().getFilterList(table)

    private var currentSearchFilter = filterList[0].apiName
    private var lastFilter = table

    //icon for recycler list
    private val icon = when(table){
        "pet" -> R.drawable.ic_round_pets
        "owner" -> R.drawable.ic_round_people
        "vet" -> R.drawable.ic_round_vet
        "breed" -> R.drawable.ic_round_paw
        else -> R.drawable.ic_round_record
    }

    //load all data from server
    fun getAllData(): MutableList<RecordItem>{
        val response = ServerApi.getData(table)
        records.addAll(ServerApi.formatListRecordItem(response))
        return records
    }

    //set filter
    fun updateFilterList(oldPosition: Int, newPosition: Int) {
        filterList[oldPosition].isSelected = false
        filterList[newPosition].isSelected = true
        currentSearchFilter = filterList[newPosition].apiName
    }

    //load data from server by user data
    fun getFilterSearchData(filter: String): MutableList<RecordItem>{
        lastFilter = "$table/filter/$currentSearchFilter/${filter.trim()}"

        val response = ServerApi.getData(lastFilter)
        updateList(ServerApi.formatListRecordItem(response))

        return records
    }

    private fun updateList(list: MutableList<RecordItem>){
        records.clear()
        records.addAll(list)
    }

    //load data from server by other activity
    fun getStartUpFilterData(filter: String): MutableList<RecordItem>{
        val response = ServerApi.getData("$table/filter/$filter")
        updateList(ServerApi.formatListRecordItem(response))
        return records
    }

    //update list for update
    fun reload(): MutableList<RecordItem> {
        val response = ServerApi.getData(lastFilter)
        updateList(ServerApi.formatListRecordItem(response))
        return records
    }

    fun isSelectedDate() = currentSearchFilter == "date"
    fun getIcon() = icon
    fun getFilterList() = filterList
}