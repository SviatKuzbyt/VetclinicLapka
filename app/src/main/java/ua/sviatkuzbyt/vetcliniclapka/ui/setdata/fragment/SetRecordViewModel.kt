package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordRepository
import ua.sviatkuzbyt.vetcliniclapka.postError
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.include.SingleLiveEvent

class SetRecordViewModel(application: Application, table: String): AndroidViewModel(application) {
    private val repository = SetRecordRepository(table)
    private var newData: RecordItem? = null

    val entryItems = MutableLiveData<List<SetRecordItem>>()
    val message =
        SingleLiveEvent<Int>()

    init { entryItems.postValue(repository.getItems()) }

    fun addData() = viewModelScope.launch(Dispatchers.IO){
        try {
            newData = repository.addRecord()
            message.postValue(R.string.added)
        } catch (e: Exception){
            postError(e, message)
            Log.e("sklt", "", e)
        }
    }

    fun getNewData() = newData

    class Factory(private val application: Application, private val table: String?)
        : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SetRecordViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SetRecordViewModel(application, table?: "") as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}