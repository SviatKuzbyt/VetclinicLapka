package ua.sviatkuzbyt.vetcliniclapka.data.repositories.create

import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem

class CreateAppointmentRepository(updateId: Int) : CreateRepository(
    "appointment", updateId,
    listOf(
        CreateRecordData("owner"),
        CreateRecordData("pet"),
        CreateRecordData("time"),
        CreateRecordData("vet"),
        CreateRecordData("complaint")
    )
){
    fun setRecord(complaint: String, isReturn: Boolean): RecordItem?{
        listForNewData.last().data = complaint
        return super.setRecord(isReturn)
    }
}