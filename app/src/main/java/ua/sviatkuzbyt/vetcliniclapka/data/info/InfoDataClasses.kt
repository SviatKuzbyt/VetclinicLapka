package ua.sviatkuzbyt.vetcliniclapka.data.info

data class InfoItems(
    val texts: List<InfoText>,
    val sharedData: List<InfoSharedData>
)

data class InfoText(
    val label: Int,
    var content: String = ""
)

data class InfoSharedData(
    val label: Int,
    val icon: Int,
    val table: String,
    val filter: String
)