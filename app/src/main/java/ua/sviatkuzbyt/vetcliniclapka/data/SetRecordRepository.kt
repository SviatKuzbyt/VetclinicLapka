package ua.sviatkuzbyt.vetcliniclapka.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.R

class SetRecordRepository(private val table: String) {

    private val entryItems = listOf(
        SetRecordItem(label = R.string.name, apiName = "name"),
        SetRecordItem(label = R.string.phone, apiName = "phone")
    )

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
    }
}

data class SetRecordItem(
    var data: String = "",
    val label: Int,
    val apiName: String,
)