package ua.sviatkuzbyt.vetcliniclapka.data.info

import ua.sviatkuzbyt.vetcliniclapka.R

class InfoRepository(private val table: String, private val recordId: Int) {
    private val items = when(table){
        "owner" -> InfoItems(listOf(
            InfoText(R.string.name, "Test name"),
            InfoText(R.string.phone, "Test phone")
        ))
        else -> InfoItems(listOf(InfoText(R.string.error)))
    }

    private val label = when(table){
        "pet" -> R.string.pet
        "owner" -> R.string.owner
        "vet" -> R.string.vet
        "medcard" -> R.string.medcard
        "appointment" -> R.string.appointment
        else -> R.string.error
    }

    fun getLabel() = label

    fun loadItems() = items
}

data class InfoItems(
    val texts: List<InfoText>
)

data class InfoText(
    val label: Int,
    var content: String = ""
)

