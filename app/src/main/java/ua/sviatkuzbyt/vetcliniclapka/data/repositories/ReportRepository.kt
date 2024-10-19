package ua.sviatkuzbyt.vetcliniclapka.data.repositories

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SavePdfException
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReportRepository(private val context: Context, private val intent: Intent) {

    fun getReport(): MutableList<String> {
        val reportItems = mutableListOf(
            "some data <br>some data <br>some data <br>some data <br>some data <br>some data",
            "some data <br>some data <br>some data <br>some data <br>some data <br>some data",
            "some data <br>some data <br>some data <br>some data <br>some data <br>some data",
        )
        reportItems.add(0, getReportInfo(reportItems.size))
        return reportItems
    }

    private fun getReportInfo(itemsCount: Int): String{
        val table = intent.getStringExtra("table")
        val filter = intent.getStringExtra("filter")
        val filterKey = intent.getStringExtra("filterKey")
        Log.v("sklt", "${intent.getStringExtra("tableApi")} ${intent.getStringExtra("filterApi")}")
        val time = getCurrentTime()

        return "<b>Таблиця:</b> $table<br>" +
                "<b>Відфільтровано за:</b> $filter<br>" +
                "<b>Значення фільтру:</b> $filterKey<br>" +
                "<b>Час створення:</b> $time<br>" +
                "<b>Кількість записів:</b> $itemsCount"
    }

    private fun getCurrentTime(): String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy")
        return current.format(formatter)
    }

    fun saveReportToPdf(list: ListView) {
        val document = PdfDocument()
        val adapter = list.adapter ?: return

        val width = list.width
        val height = (width * 1.4).toInt()
        var currentPage = 1

        var page = document.startPage(PdfDocument.PageInfo.Builder(width, height, currentPage).create())

        var layout = LinearLayout(context)
        var top = 0

        for (i in 0 until adapter.count) {
            val itemView = adapter.getView(i, null, list)

            itemView.measure(
                View.MeasureSpec.makeMeasureSpec(list.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

            if (top + itemView.measuredHeight >= height - 36){
                layout.draw(page.canvas)
                document.finishPage(page)
                top = 0
                currentPage ++
                page = document.startPage(PdfDocument.PageInfo.Builder(width, height, currentPage).create())
                layout = LinearLayout(context)
            }

            itemView.layout(0, top, list.width, top+itemView.measuredHeight) // Layout the view
            top += itemView.measuredHeight

            layout.addView(itemView)
        }

        layout.draw(page.canvas)
        document.finishPage(page)
        saveDocument(document)
    }

    private fun saveDocument(document: PdfDocument){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                savePdfInScopedStorage(context, document)
            } else{
                savePdfLegacy(document)
            }
        } catch (_: Exception){
            document.close()
            throw SavePdfException()
        }
    }

    private fun savePdfInScopedStorage(context: Context, document: PdfDocument) {
        val fileName = "report.pdf"

        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }

        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        uri?.let {
            val outputStream: OutputStream? = resolver.openOutputStream(uri)
            outputStream?.let {
                document.writeTo(it)
                it.close()
            }
        }
        document.close()
    }

    private fun savePdfLegacy(document: PdfDocument) {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory, "report.pdf")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val outputStream = FileOutputStream(file)
        document.writeTo(outputStream)
        outputStream.close()
        document.close()
    }
}