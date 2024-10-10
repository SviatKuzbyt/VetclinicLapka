package ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityCreateAppointmentBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.time.TimeFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.records.RecordsActivity

class CreateAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAppointmentBinding
    private val viewModel: CreateAppointmentViewModel by viewModels(
        factoryProducer = {
            CreateAppointmentViewModel.Factory(
                intent.getIntExtra("updateId", 0)
            )
        }
    )
    private lateinit var selectButtons: List<Button>

    private val selectActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            it.data?.let { data ->
                viewModel.setSelectData(
                    data.getIntExtra("id", 0).toString(),
                    data.getStringExtra("label") ?: "Unknown",
                    data.getIntExtra("forPosition", 0),
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectOwnerButton.setOnClickListener {
            val selectIntent = Intent(this, RecordsActivity::class.java).apply {
                putExtra("table", "owner")
                putExtra("label", getString(R.string.select_recor))
                putExtra("openMode", RecordsActivity.ACTION_SELECT)
                putExtra("forPosition", 0)
            }
            selectActivityResult.launch(selectIntent)
        }

        binding.selectPetButton.setOnClickListener {
            val ownerId = viewModel.createData.value?.get(0)?.data
            if (ownerId.isNullOrBlank())
                makeToast(this, R.string.no_text)
            else{
                val openIntent = Intent(this, RecordsActivity::class.java).apply {
                    putExtra("label", getString(R.string.select_recor))
                    putExtra("table", "pet")
                    putExtra("openMode", RecordsActivity.ACTION_SELECT)
                    putExtra("filter", "id/owner&$ownerId")
                    putExtra("forPosition", 1)
                }

                selectActivityResult.launch(openIntent)
            }
        }

        binding.selectTimeButton.setOnClickListener {
            val timeFragment = TimeFragment()
            timeFragment.show(supportFragmentManager, timeFragment.tag)
        }

        viewModel.createData.observe(this){
            if (! ::selectButtons.isInitialized)
                initButtonsList()
            val updatePosition = viewModel.getUpdatePosition()

            if (updatePosition <= CreateAppointmentViewModel.POSITION_ALL){
                for (i in selectButtons.indices){
                    setButtonText(selectButtons[i], it[i])
                }
                if (updatePosition == CreateAppointmentViewModel.POSITION_ALL_WITH_EDIT_TEXT){
                    binding.editTextComplaint.setText(it[4].data)
                }

            } else{
                setButtonText(selectButtons[updatePosition], it[updatePosition])
            }
        }

        supportFragmentManager.setFragmentResultListener("timeFr", this){ _, bundle ->
            val timeLabel = bundle.getString("time") ?: "2024-01-01 00:00:00"
            viewModel.setSelectData(timeLabel, timeLabel, 2)
        }

        binding.selectVetButton.setOnClickListener {
            val petId = viewModel.createData.value?.get(1)?.data
            val time = viewModel.createData.value?.get(2)?.data

            if (petId.isNullOrBlank() || time.isNullOrBlank())
                makeToast(this, R.string.no_text)
            else{
                val openIntent = Intent(this, RecordsActivity::class.java).apply {
                    putExtra("label", getString(R.string.select_recor))
                    putExtra("table", "vet")
                    putExtra("openMode", RecordsActivity.ACTION_SELECT)
                    putExtra("filter", "appointment/$petId&$time")
                    putExtra("forPosition", 3)
                }

                selectActivityResult.launch(openIntent)
            }
        }

        viewModel.message.observe(this){ message ->
            makeToast(this, message)
            Log.v("sklt", viewModel.getReturnData().toString())
            if (message == R.string.added){
                if (viewModel.getReturnData() != null){
                    returnRecord(viewModel.getReturnData()!!)
                } else if (viewModel.getIsUpdateData()){
                    returnIsUpdate()
                }
                finish()
            }
        }

        binding.appointmentCreateButton.setOnClickListener {
            viewModel.setRecord(
                binding.editTextComplaint.text.toString(),
                intent.getBooleanExtra("return", false)
            )
        }

        val toolBarText = if (viewModel.getIsUpdateData()) R.string.edit_record
        else R.string.create_appointment
        binding.appointmentToolbar.setupWithConfirmWindow(getString(toolBarText), this)

    }

    private fun returnIsUpdate() {
        val resultData = Intent().apply {
            putExtra("isUpdate", true)
        }
        setResult(RESULT_OK, resultData)
    }

    private fun initButtonsList(){
        binding.apply {
            selectButtons = listOf(
                selectOwnerButton,
                selectPetButton,
                selectTimeButton,
                selectVetButton
            )
        }
    }

    private fun setButtonText(button: Button, data: CreateRecordData){
        if (data.labelData.isNotBlank()) button.text = data.labelData
        else if (data.data.isNotBlank()) button.text = data.data
    }

    private fun returnRecord(item: RecordItem){
        val resultData = Intent().apply {
            putExtra("id", item.id)
            putExtra("label", item.label)
            putExtra("subtext", item.subtext)
        }
        setResult(RESULT_OK, resultData)
    }
}