package ua.sviatkuzbyt.vetcliniclapka.data

import ua.sviatkuzbyt.vetcliniclapka.R

class RecordsRepository(private val table: String) {

    private val tempData = mutableListOf(
        RecordItem(1, "First", "Content 1", IC_PEOPLE),
        RecordItem(2, "Second", "Content 2", IC_VET),
        RecordItem(3, "Third", "Content 3", IC_RECORD),
    )

    fun getAllData(): MutableList<RecordItem>{
        return tempData
    }

    fun getFilterData(filter: String, value: String): MutableList<RecordItem>{
        return tempData
    }
    companion object{
        const val IC_PEOPLE = 1
        const val IC_VET = 2
        const val IC_RECORD = 3
        const val IC_DOG = 4
        const val IC_CAT = 5
        const val IC_BIRD = 6
        const val IC_RODENT = 7
    }
}

data class RecordItem(
    val id: Int,
    val label: String,
    val subtext: String,
    var icon: Int
)