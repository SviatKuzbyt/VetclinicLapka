package ua.sviatkuzbyt.vetcliniclapka.ui.info.activity

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.info.InfoItems
import ua.sviatkuzbyt.vetcliniclapka.data.info.InfoRepository
import ua.sviatkuzbyt.vetcliniclapka.postError
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

    init { loadItems() }

    private fun loadItems() = viewModelScope.launch(Dispatchers.IO){
        try {
            items.postValue(repository.loadItems())
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