package ua.sviatkuzbyt.vetcliniclapka.ui.medcard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.sviatkuzbyt.vetcliniclapka.data.CreateMedCardRepository
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.info.InfoText
import ua.sviatkuzbyt.vetcliniclapka.ui.appointment.activity.CreateAppointmentViewModel.Companion.POSITION_ALL
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.include.SingleLiveEvent

class CreateMedCardViewModel: ViewModel() {

    private val repository = CreateMedCardRepository()
    private var updatePosition = POSITION_ALL
    private var showAllContent = false

    val createData = MutableLiveData<List<CreateRecordData>>()
    val infoData = MutableLiveData<List<InfoText>>()
    val message = SingleLiveEvent<Int>()

    fun setSelectData(data: String, labelData: String?, position: Int) {
        repository.updateData(data, labelData, position)
        updatePosition = position
        createData.postValue(repository.getCreateData())
    }

    fun getUpdatePosition(): Int{
        val tempPosition = updatePosition
        updatePosition = POSITION_ALL
        return tempPosition
    }

    fun getShowAll() = showAllContent
    
}