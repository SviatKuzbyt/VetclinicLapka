package ua.sviatkuzbyt.vetcliniclapka.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.NoTextException

class CreateAppointmentRepository {
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

    fun createRecord(text: String) {
        createData.last().data = text
        ServerApi.postData("appointment/add", formatJson())
    }

    private fun formatJson(): String {
        val jsonData = JSONObject()
        for (i in 1 until  createData.size){
            if (createData[i].data.isBlank()) throw NoTextException()
            jsonData.put(createData[i].apiName, createData[i].data)
        }
        return jsonData.toString()
    }

    fun createAndReturnRecord(text: String): RecordItem {
        createData.last().data = text
        val textRes = ServerApi.postData("appointment/addreturn", formatJson())
        return Gson().fromJson(textRes, getType)
    }

    companion object{
        private val getType = object : TypeToken<RecordItem>() {}.type
    }
}

data class CreateRecordData(
    val apiName: String,
    var data: String = "",
    var labelData: String = ""
)