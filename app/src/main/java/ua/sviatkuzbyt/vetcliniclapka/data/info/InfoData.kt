package ua.sviatkuzbyt.vetcliniclapka.data.info

import ua.sviatkuzbyt.vetcliniclapka.R

class InfoData(private val table: String) {
    fun getItems() = InfoItems(
        getTexts(),
        getSharedData()
    )

    private fun getTexts() = when(table){
        "owner" -> listOf(
            InfoText(R.string.name),
            InfoText(R.string.phone)
        )

        "pet" -> listOf(
            InfoText(R.string.name),
            InfoText(R.string.breed),
            InfoText(R.string.gender),
            InfoText(R.string.date_of_birth)
        )

        "vet" -> listOf(
            InfoText(R.string.name),
            InfoText(R.string.phone),
            InfoText(R.string.is_aviable)
        )

        "appointment" -> listOf(
            InfoText(R.string.pet),
            InfoText(R.string.owner),
            InfoText(R.string.date),
            InfoText(R.string.vet),
            InfoText(R.string.complaint)
        )

        "medcard" -> listOf(
            InfoText(R.string.pet),
            InfoText(R.string.owner),
            InfoText(R.string.date),
            InfoText(R.string.vet),
            InfoText(R.string.diagnosis),
            InfoText(R.string.treatment)
        )
        else -> listOf()
    }

    private fun getSharedData() = when(table){
        "owner" -> listOf(
            InfoSharedData(
                R.string.pets,
                R.drawable.ic_pet,
                "pet",
                "ownerid"
            )
        )

        "pet" -> listOf(
            InfoSharedData(
                R.string.owner,
                R.drawable.ic_people_one,
                "owner",
                "id"
            ),
            InfoSharedData(
                R.string.medhistory,
                R.drawable.ic_medhistory,
                "medcard",
                "petid"
            ),
            InfoSharedData(
                R.string.appointment_history,
                R.drawable.ic_appointment_history,
                "appointment",
                "petid"
            )
        )

        else -> listOf()
    }
}