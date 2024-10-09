package ua.sviatkuzbyt.vetcliniclapka.ui.medcard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.CreateMedCardRepository
import ua.sviatkuzbyt.vetcliniclapka.data.InfoText
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.ui.appointment.activity.CreateAppointmentViewModel.Companion.POSITION_ALL
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.include.SingleLiveEvent
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError

class CreateMedCardViewModel: ViewModel() {

    private val repository = CreateMedCardRepository()
    private var updatePosition = POSITION_ALL
    private var returnData: RecordItem? = null

    val createData = MutableLiveData<List<CreateRecordData>>()
    val infoData = MutableLiveData<List<InfoText>>()
    val message = SingleLiveEvent<Int>()

    fun setSelectData(data: String, labelData: String?, position: Int) {
        try {
            repository.updateData(data, labelData, position)
            updatePosition = position
            createData.postValue(repository.getCreateData())
            if (position == 1) setInfoTexts()
        } catch (e: Exception){
            postError(e, message)
        }

    }

    private fun setInfoTexts() = viewModelScope.launch(Dispatchers.IO){
        try {
            infoData.postValue(repository.getInfoData())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun getUpdatePosition(): Int{
        val tempPosition = updatePosition
        updatePosition = POSITION_ALL
        return tempPosition
    }

    fun createRecordAndReturn(ill: String, cure: String) = viewModelScope.launch(Dispatchers.IO){
        try {
            returnData = repository.createAndReturnRecord(ill, cure)
            message.postValue(R.string.added)
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun createRecord(ill: String, cure: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.createRecord(ill, cure)
            message.postValue(R.string.added)
        } catch (e: Exception) {
            postError(e, message)
        }
    }

    fun getReturnData() = returnData
}