package ua.sviatkuzbyt.vetcliniclapka.data.repositories.report

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SavePdfException
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class DocumentRepository(private val list: ListView, private val context: Context) {
    private val document = PdfDocument()
    private val adapter = list.adapter

    private val fileName = "report_${System.currentTimeMillis()}.pdf"
    private val pageWidth = list.width
    private val pageHeight = (pageWidth * 1.4).toInt()

    private var topPosition = 0
    private var currentPage = 0

    private lateinit var layoutDraw: LinearLayout
    private lateinit var page: PdfDocument.Page

    init {
        createDocument()
        saveDocument()
    }

    private fun createDocument(){
        createPage()

        for (i in 0 until adapter.count){
            val itemView = adapter.getView(i, null, list)

            //set size
            itemView.measure(
                View.MeasureSpec.makeMeasureSpec(list.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

            //add items to page if no enough space
            if (topPosition + itemView.measuredHeight >= pageHeight - 50){
                finishPage()
                createPage()
                layoutDraw = LinearLayout(context)
            }

            //add item to layoutDraw
            itemView.layout(0, topPosition, list.width, topPosition+itemView.measuredHeight)
            topPosition += itemView.measuredHeight

            layoutDraw.addView(itemView)
        }
        finishPage()
    }

    private fun createPage(){
        topPosition = 50
        currentPage ++
        page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPage).create())
        layoutDraw = LinearLayout(context)
    }

    private fun finishPage(){
        layoutDraw.draw(page.canvas)
        document.finishPage(page)
    }

    private fun saveDocument(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                savePdfInScopedStorage()
            } else{
                savePdfLegacy()
            }
        } catch (_: Exception){
            document.close()
            throw SavePdfException()
        }
    }

    private fun savePdfInScopedStorage() {
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

    private fun savePdfLegacy() {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory, fileName)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val outputStream = FileOutputStream(file)
        document.writeTo(outputStream)
        outputStream.close()
        document.close()
    }
}