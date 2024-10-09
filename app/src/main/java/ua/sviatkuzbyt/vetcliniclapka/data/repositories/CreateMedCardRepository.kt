package ua.sviatkuzbyt.vetcliniclapka.data.repositories

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.data.InfoText
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.NoTextException

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

    fun getInfoData(): List<InfoText>{
        val info = ServerApi.getData("medcard/infocreate/${createData[1].data}")
        val infoList: List<String> = Gson().fromJson(info, ServerApi.getListStringType)
        for (i in infoData.indices){
            infoData[i].content = infoList[i]
        }
        return infoData
    }

    fun updateData(data: String, labelData: String?, position: Int) {
        createData[position].data = data
        labelData?.let { createData[position].labelData = it }
    }

    fun createRecord(ill: String, cure: String) {
        createData[2].data = ill
        createData[3].data = cure

        ServerApi.postData("medcard/add", formatJson())
    }

    private fun formatJson(): String {
        val jsonData = JSONObject()
        for (i in 1 until  createData.size){
            if (createData[i].data.isBlank()) throw NoTextException()
            jsonData.put(createData[i].apiName, createData[i].data)
        }
        return jsonData.toString()
    }

    fun createAndReturnRecord(ill: String, cure: String): RecordItem {
        createData[2].data = ill
        createData[3].data = cure

        val textRes = ServerApi.postData("medcard/addreturn", formatJson())
        return Gson().fromJson(textRes, ServerApi.getRecordItemType)
    }



}