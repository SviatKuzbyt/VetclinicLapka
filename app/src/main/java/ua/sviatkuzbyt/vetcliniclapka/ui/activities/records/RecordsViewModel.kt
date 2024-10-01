package ua.sviatkuzbyt.vetcliniclapka.ui.activities.records

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.FilterItem
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.RecordsRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent

class RecordsViewModel(application: Application, intent: Intent): AndroidViewModel(application) {
    private var repository = RecordsRepository(intent.getStringExtra("table") ?: "unknown")
    val records = MutableLiveData<MutableList<RecordItem>>()
    val message = SingleLiveEvent<Pair<Int, String?>>()
    val showCalendarButton = MutableLiveData(repository.isSelectedDate())

    init {
        viewModelScope.launch(Dispatchers.IO){
            getAllData()
        }
    }

    private fun getAllData(){
        try {
            records.postValue(repository.getAllData())
        } catch (e: Exception){
            message.postValue(Pair(R.string.error, e.message))
        }
    }

    fun getFilterData(filter: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                records.postValue(repository.getFilterData(filter))
            } catch (e: Exception){
                message.postValue(Pair(R.string.error, e.message))
            }
        }
    }

    fun getIcon(): Int {
        return try {
            repository.getIcon()
        } catch (e: Exception){
            message.postValue(Pair(R.string.error, e.message))
            R.drawable.ic_round_record
        }
    }

    fun getFilterList(): List<FilterItem>{
        return try {
            repository.getFilterList()
        } catch (e: Exception){
            message.postValue(Pair(R.string.error, e.message))
            listOf()
        }
    }

    fun updateFilterList(oldPosition: Int, newPosition: Int){
        try {
            repository.updateFilterList(oldPosition, newPosition)
            showCalendarButton.postValue(repository.isSelectedDate())
        } catch (e: Exception){
            message.postValue(Pair(R.string.error, e.message))
        }
    }

    class Factory(private val application: Application, private val intent: Intent)
        : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecordsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecordsViewModel(application, intent) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}