package ua.sviatkuzbyt.vetcliniclapka.ui.appointment.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.CreateAppointmentRepository
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.include.SingleLiveEvent
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError

class CreateAppointmentViewModel: ViewModel() {
    private val repository = CreateAppointmentRepository()
    private var updatePosition = POSITION_ALL
    val createData = MutableLiveData<List<CreateRecordData>>()
    val message = SingleLiveEvent<Int>()
    private var returnData: RecordItem? = null

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
    companion object{
        const val POSITION_ALL = -1
    }
}