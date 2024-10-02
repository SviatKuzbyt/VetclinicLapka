package ua.sviatkuzbyt.vetcliniclapka.ui.fragments.set

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent

class SetRecordViewModel(application: Application, table: String): AndroidViewModel(application) {
    private val repository = SetRecordRepository(table)
    private var newData: RecordItem? = null

    val entryItems = MutableLiveData<List<SetRecordItem>>()
    val message = SingleLiveEvent<Int>()

    init { entryItems.postValue(repository.getItems()) }

    fun addData() = viewModelScope.launch(Dispatchers.IO){
        try {
            newData = repository.addRecord()
            message.postValue(R.string.added)
        } catch (_: Exception){
            message.postValue(R.string.error)
        }
    }

    fun getNewData() = newData

    class Factory(private val application: Application, private val table: String)
        : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SetRecordViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SetRecordViewModel(application, table) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}