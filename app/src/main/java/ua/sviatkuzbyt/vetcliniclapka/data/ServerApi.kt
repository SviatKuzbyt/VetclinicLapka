package ua.sviatkuzbyt.vetcliniclapka.data

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

object ServerApi {
    private const val BASIC_URL = "http://sviat-fedora.local:3000/"

    fun postData(path: String, dataToPost: String): String{
        Log.v("sklt dataToPost", dataToPost)
        val url = URL("$BASIC_URL$path")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true
        val outputStream = connection.outputStream
        outputStream.write(dataToPost.toByteArray())
        outputStream.flush()
        outputStream.close()

        val inputStream = connection.inputStream
        val response = inputStream.bufferedReader().readText()
        inputStream.close()

        Log.v("sklt response", response)

        return response
    }

    fun getData(path: String) = URL("$BASIC_URL$path").readText()
}