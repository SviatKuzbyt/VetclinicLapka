package ua.sviatkuzbyt.vetcliniclapka.data.prelists

import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.FilterItem

class RecordsFilter {
    fun getFilterList(table: String) = when(table){
        "pet" -> mutableListOf(
            FilterItem(R.string.name, "name", true),
            FilterItem(R.string.owner, "owner", false),
            FilterItem(R.string.breed, "breed", false)
        )

        "owner" -> mutableListOf(
            FilterItem(R.string.name, "name", true),
            FilterItem(R.string.phone, "phone", false)
        )

        "vet" -> mutableListOf(
            FilterItem(R.string.name, "name", true),
            FilterItem(R.string.phone, "phone", false),
            FilterItem(R.string.speciality, "specie", false)
        )

        "medcard" -> mutableListOf(
            FilterItem(R.string.date, "date", true),
            FilterItem(R.string.pet, "pet", false),
            FilterItem(R.string.vet, "vet", false),
            FilterItem(R.string.diagnosis, "diagnosis", false),
            FilterItem(R.string.owner, "owner", false)
        )
        "appointment" -> mutableListOf(
            FilterItem(R.string.date, "date", true),
            FilterItem(R.string.pet, "pet", false),
            FilterItem(R.string.vet, "vet", false),
            FilterItem(R.string.complaint, "complaint", false),
            FilterItem(R.string.owner, "owner", false),
            FilterItem(R.string.vet_today, "vettoday", false)
        )
        "breed" -> mutableListOf(
            FilterItem(R.string.name, "name", true),
            FilterItem(R.string.specie, "specie", false),
        )
        else -> mutableListOf(FilterItem(R.string.error, "none", false))
    }
}