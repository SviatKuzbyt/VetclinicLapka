package ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ConstState
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityCreateAppointmentBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.time.TimeFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.openSelectActivity

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

    //get selected item
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

        setupViews()
        setupViewModel()
    }

    private fun setupViews(){
        //select buttons
        binding.selectOwnerButton.setOnClickListener {
            openSelectActivity(
                "owner",
                null,
                0,
                selectActivityResult,
                this
            )
        }

        binding.selectPetButton.setOnClickListener {
            val ownerId = viewModel.createData.value?.get(0)?.data
            if (ownerId.isNullOrBlank())
                makeToast(this, R.string.no_text)
            else{
                openSelectActivity(
                    "pet",
                    "id/owner&$ownerId",
                    1,
                    selectActivityResult,
                    this
                )
            }
        }

        binding.selectTimeButton.setOnClickListener {
            val timeFragment = TimeFragment()
            timeFragment.show(supportFragmentManager, timeFragment.tag)
        }

        binding.selectVetButton.setOnClickListener {
            val petId = viewModel.createData.value?.get(1)?.data
            val time = viewModel.createData.value?.get(2)?.data

            if (petId.isNullOrBlank() || time.isNullOrBlank())
                makeToast(this, R.string.no_text)
            else{
                openSelectActivity(
                    "vet",
                    "appointment/$petId&$time",
                    3,
                    selectActivityResult,
                    this
                )
            }
        }

        //getting selected time
        supportFragmentManager.setFragmentResultListener("timeFr", this){ _, bundle ->
            val timeLabel = bundle.getString("time") ?: "2024-01-01 00:00:00"
            viewModel.setSelectData(timeLabel, timeLabel, 2)
        }

        //create button
        binding.appointmentCreateButton.setOnClickListener {
            viewModel.setRecord(
                binding.editTextComplaint.text.toString(),
                intent.getBooleanExtra("return", false)
            )
        }

        //tollBar
        val toolBarText = if (viewModel.getIsUpdateData()){
            R.string.edit_record
        } else{
            R.string.create_appointment
        }
        binding.appointmentToolbar.setupWithConfirmWindow(getString(toolBarText), this)
    }

    private fun setupViewModel(){
        //create data
        viewModel.createData.observe(this){
            if (! ::selectButtons.isInitialized){
                initButtonsList()
            }
            val updatePosition = viewModel.getUpdatedPosition()

            //set in activity
            if (updatePosition <= ConstState.CREATE_POSITION_ALL){
                for (i in selectButtons.indices){
                    setButtonText(selectButtons[i], it[i])
                }
                if (updatePosition == ConstState.CREATE_POSITION_ALL_WITH_EDIT_TEXT){
                    binding.editTextComplaint.setText(it[4].data)
                }

            } else{
                setButtonText(selectButtons[updatePosition], it[updatePosition])
            }
        }

        //error and complete message
        viewModel.message.observe(this){ message ->
            makeToast(this, message)
            if (message == R.string.added){
                if (viewModel.getReturnedData() != null){
                    returnRecord(viewModel.getReturnedData()!!)
                } else if (viewModel.getIsUpdateData()){
                    returnIsUpdate()
                }
                finish()
            }
        }
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

    private fun returnIsUpdate() {
        val resultData = Intent().apply {
            putExtra("isUpdate", true)
        }
        setResult(RESULT_OK, resultData)
    }
}