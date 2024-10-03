package ua.sviatkuzbyt.vetcliniclapka.ui.records.activity

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
import ua.sviatkuzbyt.vetcliniclapka.data.record.FilterItem
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordsRepository
import ua.sviatkuzbyt.vetcliniclapka.postError
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.include.SingleLiveEvent

class RecordsViewModel(application: Application, intent: Intent): AndroidViewModel(application) {
    private val table = intent.getStringExtra("table") ?: "Unknown"
    private val label = intent.getStringExtra("label") ?: "Unknown"
    private val repository = RecordsRepository(table)

    val records = MutableLiveData<MutableList<RecordItem>>()
    val showCalendarButton = MutableLiveData(repository.isSelectedDate())
    val message = SingleLiveEvent<Int>()

    init {
        viewModelScope.launch(Dispatchers.IO){ getAllData() }
    }

    //Load data
    private fun getAllData(){
        try {
            records.postValue(repository.getAllData())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun getFilterData(filter: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                records.postValue(repository.getFilterData(filter))
            } catch (e: Exception){
                postError(e, message)
            }
        }
    }

    //Filter List
    fun getFilterList(): List<FilterItem>{
        return try {
            repository.getFilterList()
        } catch (e: Exception){
            postError(e, message)
            listOf()
        }
    }

    fun updateFilterList(oldPosition: Int, newPosition: Int){
        try {
            repository.updateFilterList(oldPosition, newPosition)
            showCalendarButton.postValue(repository.isSelectedDate())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    //For views
    fun getIcon(): Int {
        return try {
            repository.getIcon()
        } catch (e: Exception){
            postError(e, message)
            R.drawable.ic_round_record
        }
    }

    fun getLabel() = label
    fun getTable() = table

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