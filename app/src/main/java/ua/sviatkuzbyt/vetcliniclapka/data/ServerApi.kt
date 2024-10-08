package ua.sviatkuzbyt.vetcliniclapka.data

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

object ServerApi {
    private const val BASIC_URL = "http://192.168.180.1:3000/"

    fun postData(path: String, dataToPost: String) =
        sendData(path, dataToPost, "POST")

    fun putData(path: String, dataToPut: String) =
        sendData(path, dataToPut, "PUT")

    fun getData(path: String) = URL("$BASIC_URL$path").readText()

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
}