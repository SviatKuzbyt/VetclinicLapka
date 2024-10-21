package ua.sviatkuzbyt.vetcliniclapka.data.repositories.report

import android.content.Context
import android.content.Intent
import android.widget.ListView
import ua.sviatkuzbyt.vetcliniclapka.data.ServerApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReportRepository(private val context: Context, private val intent: Intent) {

    fun getReport(): MutableList<String> {
        val filterKey = intent.getStringExtra("filterKey") ?: ""
        val reportItems = getItems(filterKey)
        reportItems.add(0, getReportInfo(filterKey, reportItems.size))
        return reportItems
    }

    //load report items
    private fun getItems(filterKey: String): MutableList<String> {
        val tableApi = intent.getStringExtra("tableApi")
        val filter = if(filterKey.isBlank()) "all"
        else intent.getStringExtra("filterApi")?: "all"

        val response = ServerApi.getData("$tableApi/report/$filter/$filterKey")
        return ServerApi.formatMutableListString(response)
    }

    //generate report info
    private fun getReportInfo(filterKey: String, itemsCount: Int): String{
        val table = intent.getStringExtra("table")
        val filter = intent.getStringExtra("filter")
        val time = getCurrentTime()
        val formatedKey = filterKey.ifBlank { "Всі записи" }

        return "<b>Таблиця:</b> $table<br>" +
                "<b>Відфільтровано за:</b> $filter<br>" +
                "<b>Значення фільтру:</b> $formatedKey<br>" +
                "<b>Час створення:</b> $time<br>" +
                "<b>Кількість записів:</b> $itemsCount"
    }

    private fun getCurrentTime(): String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy")
        return current.format(formatter)
    }

    fun saveReportToPdf(list: ListView) {
        DocumentRepository(list, context)
    }
}