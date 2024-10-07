package ua.sviatkuzbyt.vetcliniclapka.data.info

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
        val infoList: List<String> = Gson().fromJson(info, getType)

        for (i in 0 until items.texts.size){
            items.texts[i].content = infoList[i]
        }

        return items
    }

    companion object{
        private val getType = object : TypeToken<List<String>>() {}.type
    }
}


