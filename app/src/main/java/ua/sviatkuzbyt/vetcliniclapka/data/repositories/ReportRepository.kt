package ua.sviatkuzbyt.vetcliniclapka.data.repositories

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ListView
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SavePdfException
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ReportRepository(private val context: Context) {

    fun getReport() = mutableListOf(
        "<b>some</b> data 1", "some data 2", "some data 3"
    )

    fun saveReportToPdf(list: ListView) {
        val document = PdfDocument()
        val adapter = list.adapter ?: return
        val itemCount = adapter.count

        for (i in 0 until itemCount) {
            val itemView = adapter.getView(i, null, list)

            itemView.measure(
                View.MeasureSpec.makeMeasureSpec(list.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            itemView.layout(0, 0, list.width, itemView.measuredHeight) // Layout the view

            val pageInfo = PdfDocument.PageInfo.Builder(list.width, itemView.measuredHeight, i + 1).create()
            val page = document.startPage(pageInfo)

            itemView.draw(page.canvas)
            document.finishPage(page)
        }

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