package ua.sviatkuzbyt.vetcliniclapka.data.repositories.create

import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.data.InfoText
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem

class CreateMedCardRepository(updateId: Int): CreateRepository(
    "medcard", updateId,
    listOf(
        CreateRecordData("vet"),
        CreateRecordData("appointment"),
        CreateRecordData("ill"),
        CreateRecordData("cure")
    )
) {
    override fun loadData(){
        super.loadData()
        getInfoData()
    }

    //save and load data for information about patient
    private val infoData = listOf(
        InfoText(R.string.name),
        InfoText(R.string.breed),
        InfoText(R.string.gender),
        InfoText(R.string.date_of_birth),
        InfoText(R.string.complaint)
    )

    fun getInfoData(): List<InfoText>{
        val response = ServerApi.getData("medcard/infocreate/${listForNewData[1].data}")
        val infoList: List<String> = ServerApi.formatListString(response)

        for (i in infoData.indices){
            infoData[i].content = infoList[i]
        }
        return infoData
    }

    fun setRecord(ill: String, cure: String, isReturn: Boolean): RecordItem? {
        listForNewData[2].data = ill
        listForNewData[3].data = cure
        return super.setRecord(isReturn)
    }
}