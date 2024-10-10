package ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.sviatkuzbyt.vetcliniclapka.data.ConstState
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.repositories.create.CreateRepository
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.SingleLiveEvent

open class CreateViewModel(
    editId: Int,
    protected val repository: CreateRepository
): ViewModel() {

    protected var updatePosition = ConstState.CREATE_POSITION_ALL
    protected var returnData: RecordItem? = null
    private val isUpdateData = editId > 0

    val createData = MutableLiveData<List<CreateRecordData>>()
    val message = SingleLiveEvent<Int>()

     open fun setSelectData(data: String, labelData: String?, position: Int){
        repository.updateData(data, labelData, position)
        updatePosition = position
        createData.postValue(repository.getCreatedData())
    }

    fun getUpdatedPosition(): Int{
        val tempPosition = updatePosition
        updatePosition = ConstState.CREATE_POSITION_ALL
        return tempPosition
    }

    fun getReturnedData() = returnData
    fun getIsUpdateData() = isUpdateData
}