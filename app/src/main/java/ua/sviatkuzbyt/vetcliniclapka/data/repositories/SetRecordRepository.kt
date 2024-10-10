package ua.sviatkuzbyt.vetcliniclapka.data.repositories

import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.NoTextException
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ConstState
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem

class SetRecordRepository(private val table: String, private val editId: Int) {

    private val entryItems = when(table){
        "owner" -> listOf(
            SetRecordItem(label = R.string.name, apiName = "name", type = ConstState.SET_TYPE_TEXT),
            SetRecordItem(label = R.string.phone, apiName = "phone", type = ConstState.SET_TYPE_TEXT)
        )
        "vet" -> listOf(
            SetRecordItem(label = R.string.name, apiName = "name", type = ConstState.SET_TYPE_TEXT),
            SetRecordItem(label = R.string.phone, apiName = "phone", type = ConstState.SET_TYPE_TEXT),
            SetRecordItem(data = "0000", label = R.string.speciality, apiName = "spec", type = ConstState.SET_TYPE_CHECKBOX_SPEC)
        )
        "pet" -> listOf(
            SetRecordItem(label = R.string.name, apiName = "name", type = ConstState.SET_TYPE_TEXT),
            SetRecordItem(label = R.string.breed, apiName = "breed", type = ConstState.SET_TYPE_SELECT),
            SetRecordItem(label = R.string.owner, apiName = "owner", type = ConstState.SET_TYPE_SELECT),
            SetRecordItem(data="1", label = R.string.gender, apiName = "gender", type = ConstState.SET_TYPE_RADIO),
            SetRecordItem(label = R.string.date_of_birth, apiName = "date_of_birth", type = ConstState.SET_TYPE_TEXT),
            SetRecordItem(label = R.string.features, apiName = "features", type = ConstState.SET_TYPE_TEXT)
        )
        else -> listOf()
    }

    //load entryItems for first setup
    fun loadItems(): List<SetRecordItem>{
        if(editId != ConstState.SET_NO_EDIT_ID) loadData()
        return entryItems
    }

    private fun loadData() {
        val response = ServerApi.getData("$table/infoedit/$editId")
        val listData = ServerApi.formatListEditInfo(response)

        for (i in listData.indices){
            listData[i].data?.let { entryItems[i].data = it }
            entryItems[i].labelData = listData[i].labelData
        }
    }

    fun setRecord(): RecordItem? {
        val jsonData = JSONObject()
        entryItems.forEach {
            if (it.data.isBlank() && it.apiName != "features") throw NoTextException()
            jsonData.put(it.apiName, it.data)
        }

        if (table == "vet" && entryItems.last().data == "0000") throw NoTextException()

        //add new record
        if (editId == ConstState.SET_NO_EDIT_ID){
            val insertResult = ServerApi.postData("$table/add", jsonData.toString())
            return ServerApi.formatRecordItem(insertResult)
        }
        //update record
        else{
            ServerApi.putData("$table/update/$editId", jsonData.toString())
            return null
        }
    }

    //set new data in entryItems list
    fun updateSelectItem(dataLabel: String, position: Int, data: String) {
        entryItems[position].data = data
        entryItems[position].labelData = dataLabel
    }

    fun getItems() = entryItems
}