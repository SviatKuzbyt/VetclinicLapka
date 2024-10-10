package ua.sviatkuzbyt.vetcliniclapka.data.repositories

import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import ua.sviatkuzbyt.vetcliniclapka.data.prelists.InfoData
import ua.sviatkuzbyt.vetcliniclapka.data.InfoItems

class InfoRepository(private val table: String, private val recordId: Int) {
    private val items = InfoData(table).getItems()

    //label for toolbar
    private val label = when(table){
        "pet" -> R.string.pet
        "owner" -> R.string.owner
        "vet" -> R.string.vet
        "medcard" -> R.string.medcard
        "appointment" -> R.string.appointment
        else -> R.string.error
    }

    //load info from server
    fun loadItems(): InfoItems {
        val response = ServerApi.getData("$table/info/$recordId")
        val infoList = ServerApi.formatListString(response)

        for (i in 0 until items.texts.size){
            items.texts[i].content = infoList[i]
        }

        if (table == "vet"){
            loadAvailable()
        }
        return items
    }

    //for vet info
    private fun loadAvailable() {
        val response = ServerApi.getData("$table/available/get/$recordId")
        items.isAvailableVet = response == "1"
    }

    fun changeVetAvailable(available: Boolean) {
        items.isAvailableVet = available
        val updateData =
            if(available){
                "1"
            } else{
                "0"
            }

        ServerApi.putData(
            "$table/available/put/$recordId",
            "{\"available\": \"$updateData\"}"
        )
    }

    fun getLabel() = label
}