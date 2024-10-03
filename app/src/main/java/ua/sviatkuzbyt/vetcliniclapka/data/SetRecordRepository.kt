package ua.sviatkuzbyt.vetcliniclapka.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.R

class SetRecordRepository(private val table: String) {

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
        else -> listOf()
    }

    fun getItems() = entryItems

    fun addRecord(): RecordItem {
        val jsonData = JSONObject()
        entryItems.forEach {
            if (it.data.isBlank()) throw Exception()
            jsonData.put(it.apiName, it.data)
        }

        val insertResult = ServerApi.postData("$table/add", jsonData.toString())
        return Gson().fromJson(insertResult, getType)
    }

    companion object{
        private val getType = object : TypeToken<RecordItem>() {}.type
        const val TYPE_TEXT = 1
        const val TYPE_CHECKBOX_SPEC = 2
    }
}

data class SetRecordItem(
    var data: String = "",
    val label: Int,
    val apiName: String,
    val type: Int
)