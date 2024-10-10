package ua.sviatkuzbyt.vetcliniclapka.data.repositories.create

import com.google.gson.Gson
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

    private val infoData = listOf(
        InfoText(R.string.name),
        InfoText(R.string.breed),
        InfoText(R.string.gender),
        InfoText(R.string.date_of_birth),
        InfoText(R.string.complaint)
    )

    override fun loadData(){
        super.loadData()
        getInfoData()
    }

    fun getInfoData(): List<InfoText>{
        val info = ServerApi.getData("medcard/infocreate/${createData[1].data}")
        val infoList: List<String> = Gson().fromJson(info, ServerApi.getListStringType)

        for (i in infoData.indices){
            infoData[i].content = infoList[i]
        }
        return infoData
    }

    fun setRecord(ill: String, cure: String, isReturn: Boolean): RecordItem? {
        createData[2].data = ill
        createData[3].data = cure

        return super.setRecord(isReturn)
    }
}