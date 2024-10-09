package ua.sviatkuzbyt.vetcliniclapka.data.prelists

import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.InfoItems
import ua.sviatkuzbyt.vetcliniclapka.data.InfoSharedData
import ua.sviatkuzbyt.vetcliniclapka.data.InfoText

class InfoData(private val table: String) {
    fun getItems() = InfoItems(
        getTexts(),
        getSharedData(),
        null
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
            InfoText(R.string.speciality),
            InfoText(R.string.is_available)
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
                "pet"
            )
        )

        "pet" -> listOf(
            InfoSharedData(
                R.string.owner,
                R.drawable.ic_people_one,
                "owner"
            ),
            InfoSharedData(
                R.string.medhistory,
                R.drawable.ic_medhistory,
                "medcard"
            ),
            InfoSharedData(
                R.string.appointment_history,
                R.drawable.ic_appointment_history,
                "appointment"
            )
        )

        "vet" -> listOf(
            InfoSharedData(
                R.string.medhistory,
                R.drawable.ic_medhistory,
                "medcard"
            ),
            InfoSharedData(
                R.string.appointment_history,
                R.drawable.ic_appointment_history,
                "appointment"
            )
        )

        "appointment" -> listOf(
            InfoSharedData(
                R.string.medhistory,
                R.drawable.ic_medhistory,
                "medcard"
            ),
            InfoSharedData(
                R.string.pet,
                R.drawable.ic_pet,
                "pet"
            ),
            InfoSharedData(
                R.string.vet,
                R.drawable.ic_vet,
                "vet"
            )
        )

        "medcard" -> listOf(
            InfoSharedData(
                R.string.pet,
                R.drawable.ic_pet,
                "pet"
            ),
            InfoSharedData(
                R.string.vet,
                R.drawable.ic_vet,
                "vet"
            )
        )

        else -> listOf()
    }
}