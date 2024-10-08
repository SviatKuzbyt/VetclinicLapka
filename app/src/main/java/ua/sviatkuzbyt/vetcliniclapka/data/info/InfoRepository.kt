package ua.sviatkuzbyt.vetcliniclapka.data.info

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi

class InfoRepository(private val table: String, private val recordId: Int) {
    private val items = InfoData(table).getItems()

    private val label = when(table){
        "pet" -> R.string.pet
        "owner" -> R.string.owner
        "vet" -> R.string.vet
        "medcard" -> R.string.medcard
        "appointment" -> R.string.appointment
        else -> R.string.error
    }

    fun getLabel() = label

    fun loadItems():InfoItems{
        val info = ServerApi.getData("$table/info/$recordId")
        Log.v("sklt", info)

        val infoList: List<String> = Gson().fromJson(info, getType)

        for (i in 0 until items.texts.size){
            items.texts[i].content = infoList[i]
        }

        if (table == "vet") loadAvailable()

        return items
    }

    private fun loadAvailable() {
        val isAvailable = ServerApi.getData("$table/available/get/$recordId")
        items.isAvailableVet = isAvailable == "1"
    }

    fun changeVetAvailable(available: Boolean) {
        items.isAvailableVet = available
        val updateData = if(available) "1" else "0"
        ServerApi.putData("$table/available/put/$recordId", "{\"available\": \"$updateData\"}")
    }

    companion object{
        private val getType = object : TypeToken<List<String>>() {}.type
    }
}


