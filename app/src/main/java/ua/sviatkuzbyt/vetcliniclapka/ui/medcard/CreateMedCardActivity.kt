package ua.sviatkuzbyt.vetcliniclapka.ui.medcard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.CreateRecordData
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityCreateAppointmentBinding
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityCreateMedCardBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.appointment.activity.CreateAppointmentViewModel
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.info.recyclerviews.TextAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.records.activity.RecordsActivity

class CreateMedCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateMedCardBinding
    private lateinit var selectButtons: List<Button>
    private val viewModel: CreateMedCardViewModel by viewModels()

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
                putExtra("openMode", RecordsActivity.ACTION_SELECT)
                putExtra("filter", "available")
                putExtra("forPosition", 0)
            }
            selectActivityResult.launch(selectIntent)
        }

        viewModel.message.observe(this){
            makeToast(this, it)
            if (it == R.string.added)
                finish()
        }

        binding.medcardToolbar.setup(getString(R.string.create_medcard), this)

        viewModel.createData.observe(this){
            when(viewModel.getUpdatePosition()){
                0 -> setButtonText(binding.selectVetButton, it[0])
                1 -> setButtonText(binding.selectAppointmentButton, it[1])
                else -> {
                    setButtonText(binding.selectVetButton, it[0])
                    setButtonText(binding.selectAppointmentButton, it[1])
                }
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
                    putExtra("openMode", RecordsActivity.ACTION_SELECT)
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
    }

    private fun setButtonText(button: Button, data: CreateRecordData){
        if (data.labelData.isNotBlank()) button.text = data.labelData
    }
}