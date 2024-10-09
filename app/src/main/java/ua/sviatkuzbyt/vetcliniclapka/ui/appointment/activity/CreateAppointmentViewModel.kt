package ua.sviatkuzbyt.vetcliniclapka.ui.appointment.activity

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.CreateAppointmentRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.include.SingleLiveEvent
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError

class CreateAppointmentViewModel(editId: Int): ViewModel() {
    val createData = MutableLiveData<List<CreateRecordData>>()
    val message = SingleLiveEvent<Int>()
    private val repository = CreateAppointmentRepository(editId)
    private var updatePosition = POSITION_ALL
    private var returnData: RecordItem? = null
    private val isUpdateData = editId > 0

    fun getIsUpdateData() = isUpdateData

    init {
        if (editId > 0){
            loadEditData()
        }
    }

    private fun loadEditData() = viewModelScope.launch(Dispatchers.IO){
        try {
            repository.loadData()
            updatePosition = POSITION_ALL_WITH_EDIT_TEXT
            createData.postValue(repository.getCreateData())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun setSelectData(data: String, labelData: String?, position: Int){
        repository.updateData(data, labelData, position)
        updatePosition = position
        createData.postValue(repository.getCreateData())
    }

    fun getUpdatePosition(): Int{
        val tempPosition = updatePosition
        updatePosition = POSITION_ALL
        return tempPosition
    }

    fun createRecord(text: String) = viewModelScope.launch(Dispatchers.IO){
        try {
            repository.createRecord(text)
            message.postValue(R.string.added)
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun createRecordAndReturn(text: String) = viewModelScope.launch(Dispatchers.IO){
        try {
            returnData = repository.createAndReturnRecord(text)
            message.postValue(R.string.added)
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun getReturnData() = returnData

    class Factory(private val editId: Int)
        : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateAppointmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateAppointmentViewModel(editId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object{
        const val POSITION_ALL = -1
        const val POSITION_ALL_WITH_EDIT_TEXT = -2
    }
}