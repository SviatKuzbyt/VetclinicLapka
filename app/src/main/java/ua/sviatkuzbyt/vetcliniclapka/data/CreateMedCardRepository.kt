package ua.sviatkuzbyt.vetcliniclapka.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.info.InfoRepository
import ua.sviatkuzbyt.vetcliniclapka.data.info.InfoRepository.Companion
import ua.sviatkuzbyt.vetcliniclapka.data.info.InfoText

class CreateMedCardRepository {
    private val createData = listOf(
        CreateRecordData("vet"),
        CreateRecordData("appointment"),
        CreateRecordData("ill"),
        CreateRecordData("cure")
    )

    private val infoData = listOf(
        InfoText(R.string.name),
        InfoText(R.string.breed),
        InfoText(R.string.gender),
        InfoText(R.string.date_of_birth),
        InfoText(R.string.complaint)
    )

    fun getCreateData() = createData

    fun getInfoData(): List<InfoText>{
        val info = ServerApi.getData("appointment/infocreate/${createData[1].data}")
        val infoList: List<String> = Gson().fromJson(info, getType)
        for (i in infoData.indices){
            infoData[i].content = infoList[i]
        }
        return infoData
    }

    fun updateData(data: String, labelData: String?, position: Int) {
        createData[position].data = data
        labelData?.let { createData[position].labelData = it }
    }

    companion object{
        private val getType = object : TypeToken<List<String>>() {}.type
    }

}