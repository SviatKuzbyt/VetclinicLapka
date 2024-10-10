package ua.sviatkuzbyt.vetcliniclapka.ui.activity.info

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.data.InfoItems
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.InfoRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent

class InfoViewModel(intent: Intent) : ViewModel() {
    private val table = intent.getStringExtra("table") ?: "Unknown"
    private val recordId = intent.getIntExtra("id", 0)
    private val repository = InfoRepository(table, recordId)

    val items = MutableLiveData<InfoItems>()
    val message =
        SingleLiveEvent<Int>()

    init {
        loadItems()
    }

    //get items from server at startup or update
    fun loadItems() = viewModelScope.launch(Dispatchers.IO){
        try {
            items.postValue(repository.loadItems())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun changeVetAvailable(isAvailable: Boolean) = viewModelScope.launch(Dispatchers.IO){
        try {
            repository.changeVetAvailable(isAvailable)
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun getLabel() = repository.getLabel()
    fun getId() = recordId
    fun getTable() = table

    class Factory(private val intent: Intent)
        : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InfoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InfoViewModel(intent) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}