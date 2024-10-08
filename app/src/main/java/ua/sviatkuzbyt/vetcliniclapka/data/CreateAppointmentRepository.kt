package ua.sviatkuzbyt.vetcliniclapka.data

class CreateAppointmentRepository {
    private val createData = listOf(
        CreateRecordData("owner"),
        CreateRecordData("pet"),
        CreateRecordData("time"),
        CreateRecordData("vet"),
        CreateRecordData("complaint")
    )

    fun updateData(data: String, labelData: String?, position: Int){
        createData[position].data = data
        labelData?.let { createData[position].labelData = it }
    }

    fun getCreateData() = createData
}

data class CreateRecordData(
    val apiName: String,
    var data: String = "",
    var labelData: String = ""
)