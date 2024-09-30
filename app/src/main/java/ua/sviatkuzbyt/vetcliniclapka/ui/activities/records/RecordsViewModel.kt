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
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.RecordsRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent

class RecordsViewModel(application: Application, private val intent: Intent): AndroidViewModel(application) {
    private var repository = RecordsRepository(intent.getStringExtra("table"))
    private var _records = mutableListOf<RecordItem>()
    val records = MutableLiveData<MutableList<RecordItem>>()
    val message = SingleLiveEvent<Pair<Int, String?>>()
    private val icon = repository.getIcon()

    init {
        viewModelScope.launch(Dispatchers.IO){
            getAllData()
        }
    }

    private fun getAllData(){
        try {
            _records = repository.getAllData()
            records.postValue(_records)
        } catch (e: Exception){
            message.postValue(Pair(R.string.error, e.message))
        }
    }

    fun getIcon() = icon

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