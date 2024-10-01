package ua.sviatkuzbyt.vetcliniclapka.data

import ua.sviatkuzbyt.vetcliniclapka.R

class RecordsFilter {
    fun getFilterList(table: String) = when(table){
        "pet" -> petFilter
        "owner" -> ownerFilter
        "vet" -> vetFilter
        "medcard" -> medCardFilter
        "appointment" -> appointmentFilter
        else -> mutableListOf(FilterItem(R.string.error, "none", false))
    }

    private val petFilter by lazy { mutableListOf(
        FilterItem(R.string.name, "name", true),
        FilterItem(R.string.owner, "owner", false),
        FilterItem(R.string.breed, "breed", false)
    ) }

    private val ownerFilter by lazy { mutableListOf(
        FilterItem(R.string.name, "name", true),
        FilterItem(R.string.phone, "phone", false)
    ) }

    private val vetFilter by lazy { mutableListOf(
        FilterItem(R.string.name, "name", true),
        FilterItem(R.string.phone, "phone", false),
        FilterItem(R.string.speciality, "specie", false)
    ) }

    private val medCardFilter by lazy { mutableListOf(
        FilterItem(R.string.date, "date", true),
        FilterItem(R.string.pet, "pet", false),
        FilterItem(R.string.vet, "vet", false),
        FilterItem(R.string.diagnosis, "diagnosis", false),
        FilterItem(R.string.owner, "owner", false)
        ) }

    private val appointmentFilter by lazy { mutableListOf(
        FilterItem(R.string.date, "date", true),
        FilterItem(R.string.pet, "pet", false),
        FilterItem(R.string.vet, "vet", false),
        FilterItem(R.string.complaint, "complaint", false),
        FilterItem(R.string.owner, "owner", false),
        FilterItem(R.string.vet_today, "vettoday", false)
        ) }
}

data class FilterItem(
    val label: Int,
    val apiName: String,
    var isSelected: Boolean
)