package ua.sviatkuzbyt.vetcliniclapka.data.repositories.create

import com.google.gson.Gson
import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.EditInfo
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.NoTextException

open class CreateRepository(
    private val table: String,
    private val updateId: Int,
    protected val createData: List<CreateRecordData>
) {

    open fun loadData() {
        val data = ServerApi.getData("$table/infoedit/$updateId")
        val listData: List<EditInfo> = Gson().fromJson(data, ServerApi.getListEditInfoType)

        for (i in listData.indices){
            listData[i].data?.let { createData[i].data = it }
            createData[i].labelData = listData[i].labelData
        }
    }

    fun getCreatedData() = createData

    fun updateData(data: String, labelData: String?, position: Int){
        createData[position].data = data
        labelData?.let {
            createData[position].labelData = it
        }
    }

    protected fun setRecord(isReturn: Boolean): RecordItem? {

        return if (isReturn){
            createAndReturnRecord()
        } else if (updateId > 0){
            ServerApi.putData("$table/update/$updateId", formatJson())
            null
        } else {
            ServerApi.postData("$table/add", formatJson())
            null
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

    private fun createAndReturnRecord(): RecordItem {
        val textRes = ServerApi.postData("$table/addreturn", formatJson())
        return Gson().fromJson(textRes, ServerApi.getRecordItemType)
    }
}