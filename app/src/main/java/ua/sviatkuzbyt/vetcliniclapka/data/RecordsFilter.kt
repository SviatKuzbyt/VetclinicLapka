package ua.sviatkuzbyt.vetcliniclapka.data

import ua.sviatkuzbyt.vetcliniclapka.R

class RecordsFilter {
    fun getFilterList(table: String) = when(table){
        "pet" -> petFilter
        "owner" -> ownerFilter
        "vet" -> vetFilter
        else -> mutableListOf()
    }

    private val petFilter by lazy { mutableListOf(
        FilterItem(R.string.name, "name", true),
        FilterItem(R.string.owner, "owner", false),
        FilterItem(R.string.breed, "breed", false)
    ) }

    private val ownerFilter by lazy { mutableListOf(
        FilterItem(R.string.name, "name", true),
        FilterItem(R.string.phone, "phone", false),
    ) }

    private val vetFilter by lazy { mutableListOf(
        FilterItem(R.string.name, "name", true),
        FilterItem(R.string.phone, "phone", false),
        FilterItem(R.string.speciality, "specie", false),
    ) }
}

data class FilterItem(
    val label: Int,
    val apiName: String,
    var isSelected: Boolean
)