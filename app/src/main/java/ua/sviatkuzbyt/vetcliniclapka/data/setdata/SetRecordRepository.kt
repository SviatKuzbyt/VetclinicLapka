package ua.sviatkuzbyt.vetcliniclapka.data.setdata

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import ua.sviatkuzbyt.vetcliniclapka.NoTextException
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordItem

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
        "pet" -> listOf(
            SetRecordItem(label = R.string.name, apiName = "name", type = TYPE_TEXT),
            SetRecordItem(label = R.string.beard, apiName = "beard", type = TYPE_SELECT),
            SetRecordItem(label = R.string.owner, apiName = "owner", type = TYPE_SELECT),
            SetRecordItem(data="1", label = R.string.gender, apiName = "gender", type = TYPE_RADIO),
            SetRecordItem(label = R.string.date_of_birth, apiName = "date_of_birth", type = TYPE_DATE),
            SetRecordItem(label = R.string.features, apiName = "features", type = TYPE_TEXT)
        )
        else -> listOf()
    }

    fun getItems() = entryItems

    fun addRecord(): RecordItem {
        val jsonData = JSONObject()
        entryItems.forEach {
            if (it.data.isBlank()) throw NoTextException()
            jsonData.put(it.apiName, it.data)
        }

        val insertResult = ServerApi.postData("$table/add", jsonData.toString())
        return Gson().fromJson(insertResult, getType)
    }

    fun updateSelectItem(dataId: Int, dataLabel: String, position: Int) {
        entryItems[position].data = dataId.toString()
        entryItems[position].labelData = dataLabel
        Log.v("sklt", entryItems.toString())
    }

    companion object{
        private val getType = object : TypeToken<RecordItem>() {}.type
        const val TYPE_TEXT = 1
        const val TYPE_CHECKBOX_SPEC = 2
        const val TYPE_SELECT = 3
        const val TYPE_DATE = 4
        const val TYPE_RADIO = 5
    }
}

data class SetRecordItem(
    var data: String = "",
    var labelData: String = "",
    val label: Int,
    val apiName: String,
    val type: Int
)