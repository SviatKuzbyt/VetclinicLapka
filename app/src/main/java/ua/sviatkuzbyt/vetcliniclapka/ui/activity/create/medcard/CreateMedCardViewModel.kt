package ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.medcard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.create.CreateMedCardRepository
import ua.sviatkuzbyt.vetcliniclapka.data.InfoText
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment.CreateAppointmentViewModel.Companion.POSITION_ALL
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment.CreateAppointmentViewModel.Companion.POSITION_ALL_WITH_EDIT_TEXT
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError

class CreateMedCardViewModel(editId: Int): ViewModel() {

    private val repository = CreateMedCardRepository(editId)
    private var updatePosition = POSITION_ALL
    private var returnData: RecordItem? = null
    private val isUpdateData = editId > 0

    val createData = MutableLiveData<List<CreateRecordData>>()
    val infoData = MutableLiveData<List<InfoText>>()
    val message =
        SingleLiveEvent<Int>()

    init {
        if (editId > 0){
            loadEditData()
        }
    }

    private fun loadEditData() = viewModelScope.launch(Dispatchers.IO){
        try {
            repository.loadData()
            updatePosition = POSITION_ALL_WITH_EDIT_TEXT
            createData.postValue(repository.getCreatedData())
            infoData.postValue(repository.getInfoData())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun setSelectData(data: String, labelData: String?, position: Int) {
        try {
            repository.updateData(data, labelData, position)
            updatePosition = position
            createData.postValue(repository.getCreatedData())
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

    fun setRecord(ill: String, cure: String, isReturn: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        try {
            returnData = repository.setRecord(ill, cure, isReturn)
            message.postValue(R.string.added)
        } catch (e: Exception) {
            postError(e, message)
        }
    }

    class Factory(private val editId: Int)
        : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateMedCardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateMedCardViewModel(editId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun getReturnData() = returnData
    fun getIsUpdateData() = isUpdateData
}