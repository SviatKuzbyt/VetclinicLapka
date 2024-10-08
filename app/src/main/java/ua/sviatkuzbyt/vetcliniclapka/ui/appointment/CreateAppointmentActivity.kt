package ua.sviatkuzbyt.vetcliniclapka.ui.appointment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.data.setdata.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityCreateAppointmentBinding
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityRecordsBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.records.activity.RecordsActivity

class CreateAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAppointmentBinding
    private val viewModel: CreateAppointmentViewModel by viewModels()
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

        binding.appointmentToolbar.setup(getString(R.string.create_appointment), this)

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
            val ownerId = viewModel.createData.value?.get(0)?.data ?: "0"
            val openIntent = Intent(this, RecordsActivity::class.java).apply {
                putExtra("label", getString(R.string.select_recor))
                putExtra("table", "pet")
                putExtra("openMode", RecordsActivity.ACTION_SELECT)
                putExtra("filter", "owner&$ownerId")
                putExtra("forPosition", 1)
            }

            selectActivityResult.launch(openIntent)
        }

        viewModel.createData.observe(this){
            if (! ::selectButtons.isInitialized)
                initButtonsList()
            val updatePosition = viewModel.getUpdatePosition()

            if (updatePosition == CreateAppointmentViewModel.POSITION_ALL){
                for (i in selectButtons.indices){
                    setButtonText(selectButtons[i], it[i])
                }
            } else{
                setButtonText(selectButtons[updatePosition], it[updatePosition])
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
}