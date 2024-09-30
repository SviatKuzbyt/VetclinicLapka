package ua.sviatkuzbyt.vetcliniclapka.data

import ua.sviatkuzbyt.vetcliniclapka.R

class RecordsRepository(private val table: String?) {

    private val tempData = mutableListOf(
        RecordItem(1, "First", "Content 1"),
        RecordItem(2, "Second", "Content 2"),
        RecordItem(3, "Third", "Content 3"),
    )

    fun getAllData(): MutableList<RecordItem>{
        return tempData
    }

    fun getFilterData(filter: String, value: String): MutableList<RecordItem>{
        return tempData
    }

    fun getIcon() = when(table){
        "pet" -> R.drawable.ic_round_pets
        "owner" -> R.drawable.ic_round_people
        "vet" -> R.drawable.ic_round_vet
        else -> R.drawable.ic_round_record
    }
}

data class RecordItem(
    val id: Int,
    val label: String,
    val subtext: String
)