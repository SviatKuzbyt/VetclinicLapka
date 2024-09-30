package ua.sviatkuzbyt.vetcliniclapka.ui.activities.records

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.RecordsRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent

class RecordsViewModel(application: Application, table: String): AndroidViewModel(application) {
    private var repository = RecordsRepository(table)
    val records = MutableLiveData<MutableList<RecordItem>>()
    val message = SingleLiveEvent<Pair<Int, String?>>()

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
}