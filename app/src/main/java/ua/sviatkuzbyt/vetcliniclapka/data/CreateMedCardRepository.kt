package ua.sviatkuzbyt.vetcliniclapka.data

import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.info.InfoText

class CreateMedCardRepository {
    private val createData = listOf(
        CreateRecordData("vet"),
        CreateRecordData("appointment"),
        CreateRecordData("ill"),
        CreateRecordData("cure")
    )

    private val infoData = listOf(
        InfoText(R.string.name),
        InfoText(R.string.breed),
        InfoText(R.string.gender),
        InfoText(R.string.date_of_birth),
        InfoText(R.string.complaint)
    )

    fun getCreateData() = createData
    fun getInfoData() = infoData
    fun updateData(data: String, labelData: String?, position: Int) {
        createData[position].data = data
        labelData?.let { createData[position].labelData = it }
    }

}