package ua.sviatkuzbyt.vetcliniclapka.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import java.net.URL

object ServerApi {
    private const val BASIC_URL = "http://192.168.180.130:3000/"
    private val gson = Gson()

    private val getRecordItemType = object : TypeToken<RecordItem>() {}.type
    private val getListEditInfoType = object : TypeToken<List<EditInfo>>() {}.type
    private val getListStringType = object : TypeToken<List<String>>() {}.type
    private val getMutableListStringType = object : TypeToken<MutableList<String>>() {}.type
    private val getListRecordItemType = object : TypeToken<MutableList<RecordItem>>() {}.type

    fun getData(path: String): String{
        Log.v("sklt", "$BASIC_URL$path")
        return URL("$BASIC_URL$path").readText()
    }

    fun postData(path: String, dataToPost: String) =
        sendData(path, dataToPost, "POST")

    fun putData(path: String, dataToPut: String) =
        sendData(path, dataToPut, "PUT")

    private fun sendData(path: String, data: String, method: String): String{
        val url = URL("$BASIC_URL$path")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        val outputStream = connection.outputStream
        outputStream.write(data.toByteArray())
        outputStream.flush()
        outputStream.close()

        val inputStream = connection.inputStream
        val response = inputStream.bufferedReader().readText()
        inputStream.close()

        return response
    }

    fun formatRecordItem(text: String): RecordItem =
        gson.fromJson(text, getRecordItemType)

    fun formatListEditInfo(text: String): List<EditInfo> =
        gson.fromJson(text, getListEditInfoType)

    fun formatListString(text: String): List<String> =
        gson.fromJson(text, getListStringType)

    fun formatMutableListString(text: String): MutableList<String> =
        gson.fromJson(text, getMutableListStringType)

    fun formatListRecordItem(text: String): MutableList<RecordItem> =
        gson.fromJson(text, getListRecordItemType)
}