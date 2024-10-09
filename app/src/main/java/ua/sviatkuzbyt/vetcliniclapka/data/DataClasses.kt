package ua.sviatkuzbyt.vetcliniclapka.data

data class InfoItems(
    val texts: List<InfoText>,
    val sharedData: List<InfoSharedData>,
    var isAvailableVet: Boolean?
)

data class InfoText(
    val label: Int,
    var content: String = ""
)

data class InfoSharedData(
    val label: Int,
    val icon: Int,
    val filterTable: String
)

data class FilterItem(
    val label: Int,
    val apiName: String,
    var isSelected: Boolean
)

data class RecordItem(
    val id: Int,
    val label: String,
    val subtext: String
)

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