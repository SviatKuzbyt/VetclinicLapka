package ua.sviatkuzbyt.vetcliniclapka.ui.activity.report

import android.app.Application
import android.content.Intent
import android.widget.ListView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.report.ReportRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError

class ReportViewModel(application: Application, intent: Intent): AndroidViewModel(application) {
    val report = MutableLiveData<MutableList<String>>()
    val message = SingleLiveEvent<Int>()
    private val repository = ReportRepository(application, intent)

    init { loadReport() }

    private fun loadReport() = viewModelScope.launch(Dispatchers.IO){
        try {
            report.postValue(repository.getReport())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun saveReportToPdf(listView: ListView) = viewModelScope.launch(Dispatchers.IO){
        try {
            repository.saveReportToPdf(listView)
            message.postValue(R.string.save_pdf)
        } catch (e: Exception){
            postError(e, message)
        }
    }

    class Factory(private val application: Application, private val intent: Intent)
        : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReportViewModel(application, intent) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}