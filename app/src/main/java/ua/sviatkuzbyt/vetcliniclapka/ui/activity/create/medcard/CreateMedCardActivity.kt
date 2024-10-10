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
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.recyclers.TextAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.openSelectActivity

class CreateMedCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateMedCardBinding
    private val viewModel: CreateMedCardViewModel by viewModels(
        factoryProducer = {
            CreateMedCardViewModel.Factory(
                intent.getIntExtra("updateId", 0)
            )
        }
    )

    //get selected item
    private val selectActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
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

        setupViews()
        setupViewModel()
    }

    private fun setupViews(){
        //select buttons
        binding.selectVetButton.setOnClickListener {
            openSelectActivity(
                "vet",
                "available",
                0,
                selectActivityResult,
                this
            )
        }

        binding.selectAppointmentButton.setOnClickListener {
            val vetId = viewModel.createData.value?.get(0)?.data

            if (vetId.isNullOrBlank())
                makeToast(this, R.string.no_text)
            else{
                openSelectActivity(
                    "appointment",
                    "vetid/$vetId",
                    1,
                    selectActivityResult,
                    this
                )
            }
        }

        //create button
        binding.medcardCreateButton.setOnClickListener {
            val ill = binding.editTextIll.text.toString()
            val cure = binding.editTextCure.text.toString()
            val isReturn = intent.getBooleanExtra("return", false)

            viewModel.setRecord(ill, cure, isReturn)
        }

        //recyclerView
        binding.infoRecycler.layoutManager = LinearLayoutManager(this)

        //toolBar
        val toolBarText = if (viewModel.getIsUpdateData()){
            R.string.edit_record
        } else{
            R.string.create_medcard
        }
        binding.medcardToolbar.setupWithConfirmWindow(getString(toolBarText), this)
    }

    private fun setupViewModel(){
        //set create data
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

            //set edinfoit texts
            if (updatePosition == ConstState.CREATE_POSITION_ALL_WITH_EDIT_TEXT){
                binding.editTextIll.setText(it[2].data)
                binding.editTextCure.setText(it[3].data)
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

        //set info data
        viewModel.infoData.observe(this){
            binding.infoCreateGroup.visibility = View.VISIBLE
            binding.infoRecycler.adapter = TextAdapter(it)
        }
    }

    private fun setButtonText(button: Button, data: CreateRecordData){
        if (data.labelData.isNotBlank()) button.text = data.labelData
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