package ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.medcard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ConstState
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.create.CreateMedCardRepository
import ua.sviatkuzbyt.vetcliniclapka.data.InfoText
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.base.CreateViewModel
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.postError

class CreateMedCardViewModel(editId: Int)
    : CreateViewModel(editId, CreateMedCardRepository(editId))
{
    val infoData = MutableLiveData<List<InfoText>>()

    init {
        if (editId > 0) loadEditData()
    }

    private fun loadEditData() = viewModelScope.launch(Dispatchers.IO){
        try {
            repository.loadData()
            updatePosition = ConstState.CREATE_POSITION_ALL_WITH_EDIT_TEXT
            createData.postValue(repository.getCreatedData())

            if (repository is CreateMedCardRepository){
                infoData.postValue(repository.getInfoData())
            }
        } catch (e: Exception){
            postError(e, message)
        }
    }

    override fun setSelectData(data: String, labelData: String?, position: Int){
        super.setSelectData(data, labelData, position)
        if (position == 1) setInfoTexts()
    }

    private fun setInfoTexts() = viewModelScope.launch(Dispatchers.IO){
        try {
            if (repository is CreateMedCardRepository)
                infoData.postValue(repository.getInfoData())
        } catch (e: Exception){
            postError(e, message)
        }
    }

    fun setRecord(ill: String, cure: String, isReturn: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (repository is CreateMedCardRepository)
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
}