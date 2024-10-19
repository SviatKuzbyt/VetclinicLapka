package ua.sviatkuzbyt.vetcliniclapka.ui.activity.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.ReportRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError

class ReportViewModel: ViewModel() {
    val report = MutableLiveData<MutableList<String>>()
    val message = SingleLiveEvent<Int>()
    private val repository = ReportRepository()

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
}