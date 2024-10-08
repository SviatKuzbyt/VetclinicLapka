package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.fragment

import android.os.Bundle
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
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.include.SingleLiveEvent

class SetRecordViewModel(args: Bundle): ViewModel() {
    private val updateId = args.getInt("updateId", SetRecordRepository.NO_EDIT_ID)
    private val repository = SetRecordRepository(
        args.getString("table")?: "unknown", updateId
    )
    private var newData: RecordItem? = null
    private var updatePosition = NO_UPDATE_POSITION

    val entryItems = MutableLiveData<List<SetRecordItem>>()
    val message =
        SingleLiveEvent<Int>()

    init { init() }

    private fun init() = viewModelScope.launch(Dispatchers.IO){
        try {
            entryItems.postValue(repository.getItems())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun addData() = viewModelScope.launch(Dispatchers.IO){
        try {
            newData = repository.setRecord()
            message.postValue(
                if (newData == null) R.string.edited
                else R.string.added
            )
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun getNewData() = newData

    fun getUpdatePosition(): Int {
        val position = updatePosition
        updatePosition = NO_UPDATE_POSITION
        return position
    }

    fun updateSelectItem(dataLabel: String, position: Int, data: String) {
        try {
            repository.updateSelectItem(dataLabel, position, data)
            updatePosition = position
            entryItems.postValue(repository.getItems())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    class Factory(private val args: Bundle)
        : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SetRecordViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SetRecordViewModel(args) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object{
        const val NO_UPDATE_POSITION = -1
    }
}