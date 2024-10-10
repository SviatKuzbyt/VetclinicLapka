package ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.medcard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ConstState
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityCreateMedCardBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment.CreateAppointmentViewModel
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.recyclers.TextAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.records.RecordsActivity

class CreateMedCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateMedCardBinding
    private val viewModel: CreateMedCardViewModel by viewModels(
        factoryProducer = {
            CreateMedCardViewModel.Factory(
                intent.getIntExtra("updateId", 0)
            )
        }
    )

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
        binding = ActivityCreateMedCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectVetButton.setOnClickListener {
            val selectIntent = Intent(this, RecordsActivity::class.java).apply {
                putExtra("table", "vet")
                putExtra("label", getString(R.string.select_recor))
                putExtra("openMode", ConstState.RECORD_ACTION_SELECT)
                putExtra("filter", "available")
                putExtra("forPosition", 0)
            }
            selectActivityResult.launch(selectIntent)
        }

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

        viewModel.message.observe(this){ message ->
            makeToast(this, message)
            if (message == R.string.added){
                viewModel.getReturnedData()?.let { data ->
                    returnRecord(data)
                }
                finish()
            }
        }
        val toolBarText = if (viewModel.getIsUpdateData()) R.string.edit_record
        else R.string.create_medcard
        binding.medcardToolbar.setupWithConfirmWindow(getString(toolBarText), this)

        viewModel.createData.observe(this){
            val updatePosition = viewModel.getUpdatedPosition()
            when(updatePosition){
                0 -> setButtonText(binding.selectVetButton, it[0])
                1 -> setButtonText(binding.selectAppointmentButton, it[1])
                else -> {
                    setButtonText(binding.selectVetButton, it[0])
                    setButtonText(binding.selectAppointmentButton, it[1])
                }
            }

            if (updatePosition == ConstState.CREATE_POSITION_ALL_WITH_EDIT_TEXT){
                binding.editTextIll.setText(it[2].data)
                binding.editTextCure.setText(it[3].data)
            }
        }

        binding.selectAppointmentButton.setOnClickListener {
            val vetId = viewModel.createData.value?.get(0)?.data

            if (vetId.isNullOrBlank())
                makeToast(this, R.string.no_text)
            else{
                val selectIntent = Intent(this, RecordsActivity::class.java).apply {
                    putExtra("table", "appointment")
                    putExtra("label", getString(R.string.select_recor))
                    putExtra("openMode", ConstState.RECORD_ACTION_SELECT)
                    putExtra("filter", "vetid/$vetId")
                    putExtra("forPosition", 1)
                }
                selectActivityResult.launch(selectIntent)
            }
        }

        binding.infoRecycler.layoutManager = LinearLayoutManager(this)

        viewModel.infoData.observe(this){
            binding.infoCreateGroup.visibility = View.VISIBLE
            binding.infoRecycler.adapter = TextAdapter(it)
        }

        binding.medcardCreateButton.setOnClickListener {
            val ill = binding.editTextIll.text.toString()
            val cure = binding.editTextCure.text.toString()
            val isReturn = intent.getBooleanExtra("return", false)

            viewModel.setRecord(ill, cure, isReturn)
        }
    }

    private fun returnRecord(item: RecordItem){
        val resultData = Intent().apply {
            putExtra("id", item.id)
            putExtra("label", item.label)
            putExtra("subtext", item.subtext)
        }
        setResult(RESULT_OK, resultData)
    }

    private fun setButtonText(button: Button, data: CreateRecordData){
        if (data.labelData.isNotBlank()) button.text = data.labelData
    }

    private fun returnIsUpdate() {
        val resultData = Intent().apply {
            putExtra("isUpdate", true)
        }
        setResult(RESULT_OK, resultData)
    }
}