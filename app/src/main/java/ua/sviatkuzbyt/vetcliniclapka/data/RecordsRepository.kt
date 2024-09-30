package ua.sviatkuzbyt.vetcliniclapka.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ua.sviatkuzbyt.vetcliniclapka.R
import java.net.URL

class RecordsRepository(private val table: String) {

    fun getAllData(): MutableList<RecordItem>{
        val text = URL("http://sviat-fedora.local:3000/$table").readText()
        val gson = Gson()
        return gson.fromJson(text, getType)
    }

    fun getFilterData(filter: String, value: String): MutableList<RecordItem>{
        return mutableListOf()
    }

    fun getIcon() = when(table){
        "pet" -> R.drawable.ic_round_pets
        "owner" -> R.drawable.ic_round_people
        "vet" -> R.drawable.ic_round_vet
        else -> R.drawable.ic_round_record
    }

    companion object{
        private val getType = object : TypeToken<MutableList<RecordItem>>() {}.type
    }
}

data class RecordItem(
    val id: Int,
    val label: String,
    val subtext: String
)