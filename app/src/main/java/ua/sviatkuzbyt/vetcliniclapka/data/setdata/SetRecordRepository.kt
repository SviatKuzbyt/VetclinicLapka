package ua.sviatkuzbyt.vetcliniclapka.data.setdata

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.NoTextException
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordItem
import java.io.FileNotFoundException
import kotlin.jvm.Throws

class SetRecordRepository(private val table: String, private val editId: Int) {

    private val entryItems = when(table){
        "owner" -> listOf(
            SetRecordItem(label = R.string.name, apiName = "name", type = TYPE_TEXT),
            SetRecordItem(label = R.string.phone, apiName = "phone", type = TYPE_TEXT)
        )
        "vet" -> listOf(
            SetRecordItem(label = R.string.name, apiName = "name", type = TYPE_TEXT),
            SetRecordItem(label = R.string.phone, apiName = "phone", type = TYPE_TEXT),
            SetRecordItem(data = "0000", label = R.string.speciality, apiName = "spec", type = TYPE_CHECKBOX_SPEC)
        )
        "pet" -> listOf(
            SetRecordItem(label = R.string.name, apiName = "name", type = TYPE_TEXT),
            SetRecordItem(label = R.string.breed, apiName = "breed", type = TYPE_SELECT),
            SetRecordItem(label = R.string.owner, apiName = "owner", type = TYPE_SELECT),
            SetRecordItem(data="1", label = R.string.gender, apiName = "gender", type = TYPE_RADIO),
            SetRecordItem(label = R.string.date_of_birth, apiName = "date_of_birth", type = TYPE_TEXT),
            SetRecordItem(label = R.string.features, apiName = "features", type = TYPE_TEXT)
        )
        else -> listOf()
    }

    fun loadItems(): List<SetRecordItem>{
        if(editId != NO_EDIT_ID) loadData()
        return entryItems
    }

    fun getItems() = entryItems

    private fun loadData() {
        val data = ServerApi.getData("$table/infoedit/$editId")
        Log.v("sklt", data)
        val listData: List<EditInfo> = Gson().fromJson(data, getTypeString)

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

        if (editId == NO_EDIT_ID){
            val insertResult = ServerApi.postData("$table/add", jsonData.toString())
            return Gson().fromJson(insertResult, getType)
        } else{
            ServerApi.putData("$table/update/$editId", jsonData.toString())
            return null
        }
    }

    fun updateSelectItem(dataLabel: String, position: Int, data: String) {
        entryItems[position].data = data
        entryItems[position].labelData = dataLabel
    }

    companion object{
        private val getType = object : TypeToken<RecordItem>() {}.type
        private val getTypeString = object : TypeToken<List<EditInfo>>() {}.type
        const val TYPE_TEXT = 1
        const val TYPE_CHECKBOX_SPEC = 2
        const val TYPE_SELECT = 3
        const val TYPE_RADIO = 5

        const val NO_EDIT_ID = 0
    }
}

data class EditInfo(
    val data: String? = "",
    val labelData: String = ""
)

data class SetRecordItem(
    var data: String = "",
    var labelData: String = "",
    val label: Int,
    val apiName: String,
    val type: Int
)