package ua.sviatkuzbyt.vetcliniclapka.ui.activity.report

import android.app.Application
import android.widget.ListView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.ReportRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError

class ReportViewModel(application: Application): AndroidViewModel(application) {
    val report = MutableLiveData<MutableList<String>>()
    val message = SingleLiveEvent<Int>()
    private val repository = ReportRepository(application)

    init {
        loadReport()
    }

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
}