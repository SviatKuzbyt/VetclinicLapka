package ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ConstState
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.create.CreateAppointmentRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.base.CreateViewModel
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError

class CreateAppointmentViewModel(editId: Int) : CreateViewModel(
    editId, CreateAppointmentRepository(editId)
) {

    init {
        if (editId > 0) loadEditData()
    }

    private fun loadEditData() = viewModelScope.launch(Dispatchers.IO){
        try {
            repository.loadData()
            updatePosition = ConstState.CREATE_POSITION_ALL_WITH_EDIT_TEXT
            createData.postValue(repository.getCreatedData())
        } catch (e: Exception){
            postError(e, message)
        }
    }

     fun setRecord(text: String, isReturn: Boolean) = viewModelScope.launch(Dispatchers.IO){
        try {
            if (repository is CreateAppointmentRepository){
                returnData = repository.setRecord(text, isReturn)
                message.postValue(R.string.added)
            }
        } catch (e: Exception){
            postError(e, message)
        }
    }

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
}