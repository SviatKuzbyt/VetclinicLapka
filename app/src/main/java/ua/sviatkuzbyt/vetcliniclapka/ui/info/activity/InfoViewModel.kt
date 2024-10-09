package ua.sviatkuzbyt.vetcliniclapka.ui.info.activity

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.InfoItems
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.InfoRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.include.SingleLiveEvent

class InfoViewModel(intent: Intent) : ViewModel() {
    private val table = intent.getStringExtra("table") ?: "Unknown"
    private val recordId = intent.getIntExtra("id", 0)
    private val repository = InfoRepository(table, recordId)

    val items = MutableLiveData<InfoItems>()
    val message = SingleLiveEvent<Int>()

    fun getLabel() =
        try { repository.getLabel()}
        catch (_: Exception){ R.string.error }

    fun getId() = recordId
    fun getTable() = table

    init { loadItems() }

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