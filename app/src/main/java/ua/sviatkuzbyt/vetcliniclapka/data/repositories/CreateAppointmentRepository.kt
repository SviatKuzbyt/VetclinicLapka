package ua.sviatkuzbyt.vetcliniclapka.data.repositories

import com.google.gson.Gson
import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.EditInfo
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.NoTextException

class CreateAppointmentRepository(private val updateId: Int) {

    private val createData = listOf(
        CreateRecordData("owner"),
        CreateRecordData("pet"),
        CreateRecordData("time"),
        CreateRecordData("vet"),
        CreateRecordData("complaint")
    )

    fun updateData(data: String, labelData: String?, position: Int){
        createData[position].data = data
        labelData?.let { createData[position].labelData = it }
    }

    fun getCreateData() = createData

    fun loadData() {
        val data = ServerApi.getData("appointment/infoedit/$updateId")
        val listData: List<EditInfo> = Gson().fromJson(data, ServerApi.getListEditInfoType)

        for (i in listData.indices){
            listData[i].data?.let { createData[i].data = it }
            createData[i].labelData = listData[i].labelData
        }
    }

    private fun formatJson(): String {
        val jsonData = JSONObject()
        for (i in 1 until  createData.size){
            if (createData[i].data.isBlank()) throw NoTextException()
            jsonData.put(createData[i].apiName, createData[i].data)
        }
        return jsonData.toString()
    }

    fun createAndReturnRecord(): RecordItem {
        val textRes = ServerApi.postData("appointment/addreturn", formatJson())
        return Gson().fromJson(textRes, ServerApi.getRecordItemType)
    }

    fun setRecord(complaint: String, isReturn: Boolean): RecordItem? {
        createData.last().data = complaint

        return if (isReturn){
            createAndReturnRecord()
        } else if (updateId > 0){
            ServerApi.putData("appointment/update/$updateId", formatJson())
            null
        } else {
            ServerApi.postData("appointment/add", formatJson())
            null
        }
    }
}