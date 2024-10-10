package ua.sviatkuzbyt.vetcliniclapka.ui.fragments.setdata

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ConstState
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.SetRecordRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent

class SetRecordViewModel(args: Bundle): ViewModel() {
    private val updateId = args.getInt("updateId", ConstState.SET_NO_EDIT_ID)
    private val label = args.getInt("label", R.string.create_record)
    private val repository = SetRecordRepository(
        args.getString("table")?: "unknown", updateId
    )
    private var newData: RecordItem? = null
    private var updatePosition = ConstState.SET_NO_UPDATE_POSITION
    val entryItems = MutableLiveData<List<SetRecordItem>>()
    val message = SingleLiveEvent<Int>()

    //load list from repository
    init {
        viewModelScope.launch(Dispatchers.IO){
            try {
                entryItems.postValue(repository.loadItems())
            } catch (e: Exception){
                postError(e, message)
            }
        }
    }

    //save data to server
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

    //set data to repository
    fun updateSelectItem(dataLabel: String, position: Int, data: String) {
        try {
            repository.updateSelectItem(dataLabel, position, data)
            updatePosition = position
            entryItems.postValue(repository.getItems())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    //get private values
    fun getUpdatePosition(): Int {
        val position = updatePosition
        updatePosition = ConstState.SET_NO_UPDATE_POSITION
        return position
    }

    fun getLabel() = label
    fun getNewData() = newData

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
}