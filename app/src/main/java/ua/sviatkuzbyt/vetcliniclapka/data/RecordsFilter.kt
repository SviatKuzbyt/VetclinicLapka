package ua.sviatkuzbyt.vetcliniclapka.data

import ua.sviatkuzbyt.vetcliniclapka.R

class RecordsFilter {
    fun getFilterList(table: String) = when(table){
        "pet" -> petFilter
        else -> mutableListOf()
    }

    private val petFilter by lazy { mutableListOf(
        FilterItem(R.string.name, "name", true),
        FilterItem(R.string.owner, "owner", false),
        FilterItem(R.string.breed, "breed", false)
    ) }
}

data class FilterItem(
    val label: Int,
    val apiName: String,
    var isSelected: Boolean
)