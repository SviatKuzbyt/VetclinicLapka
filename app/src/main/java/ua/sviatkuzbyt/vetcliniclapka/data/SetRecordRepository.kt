package ua.sviatkuzbyt.vetcliniclapka.data

import ua.sviatkuzbyt.vetcliniclapka.R

class SetRecordRepository(private val table: String) {

    private val entryItems = listOf(
        SetRecordItem(label = R.string.name, apiName = "name"),
        SetRecordItem(label = R.string.phone, apiName = "phone")
    )

    fun getItems() = entryItems

    //TEMP
    fun addRecord(): RecordItem{
        val id = 0
        return RecordItem(id, entryItems[0].data, entryItems[1].data)
    }
}

data class SetRecordItem(
    var data: String = "",
    val label: Int,
    val apiName: String,
)