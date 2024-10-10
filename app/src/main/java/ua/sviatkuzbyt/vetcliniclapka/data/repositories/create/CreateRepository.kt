package ua.sviatkuzbyt.vetcliniclapka.data.repositories.create

import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.NoTextException

open class CreateRepository(
    private val table: String,
    private val updateId: Int,
    protected val listForNewData: List<CreateRecordData>
) {

    //load data for edit record
    open fun loadData() {
        val response = ServerApi.getData("$table/infoedit/$updateId")
        val listData = ServerApi.formatListEditInfo(response)

        for (i in listData.indices){
            listData[i].data?.let { listForNewData[i].data = it }
            listForNewData[i].labelData = listData[i].labelData
        }
    }

    //set new data in createData list
    fun updateData(data: String, labelData: String?, position: Int){
        listForNewData[position].data = data
        labelData?.let {
            listForNewData[position].labelData = it
        }
    }

    //save data to server
    protected fun setRecord(isReturn: Boolean): RecordItem? {
        //save and return to apply changes in RecordActivity
        return if (isReturn){
            createAndReturnRecord()
        }
        //Update data on server
        else if (updateId > 0){
            ServerApi.putData("$table/update/$updateId", formatJson())
            null
        }
        //Just save data
        else {
            ServerApi.postData("$table/add", formatJson())
            null
        }
    }

    //create json from listForNewData
    private fun formatJson(): String {
        val jsonData = JSONObject()
        for (i in 1 until  listForNewData.size){
            if (listForNewData[i].data.isBlank()) throw NoTextException()
            jsonData.put(listForNewData[i].apiName, listForNewData[i].data)
        }
        return jsonData.toString()
    }

    private fun createAndReturnRecord(): RecordItem {
        val textRes = ServerApi.postData("$table/addreturn", formatJson())
        return ServerApi.formatRecordItem(textRes)
    }

    fun getCreatedData() = listForNewData
}