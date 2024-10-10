package ua.sviatkuzbyt.vetcliniclapka.ui.activity.info

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityInfoBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment.CreateAppointmentActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.recyclers.info.SharedDataAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.recyclers.TextAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.medcard.CreateMedCardActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.records.RecordsActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.setdata.SetRecordFragment

class InfoActivity : AppCompatActivity(), SharedDataAdapter.Action,
    SetRecordFragment.SetRecordActions {
    private lateinit var binding: ActivityInfoBinding
    private val viewModel: InfoViewModel by viewModels {
        InfoViewModel.Factory(intent)
    }

    private val createActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val resultData = it.data
                if (resultData?.getBooleanExtra("isUpdate", false) == true){
                    update()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupViewModel()
    }

    private fun setupViews(){
        binding.apply {
            //ToolBar
            infoToolBar.setup(getString(viewModel.getLabel()), this@InfoActivity)

            //Info frame
            infoTextFrame.apply {
                frameRecycler.layoutManager = LinearLayoutManager(this@InfoActivity)
                frameLabel.setText(R.string.info)
            }

            //Shared data frame
            infoDataFrame.apply {
                frameRecycler.layoutManager = LinearLayoutManager(this@InfoActivity)
                frameLabel.setText(R.string.shared_data)
            }

            //action frame
            infoActionFrame.apply {
                frameEditButton.setOnClickListener{
                    when(viewModel.getTable()){
                        "appointment" -> openCreateActivity(CreateAppointmentActivity::class.java)
                        "medcard" -> openCreateActivity(CreateMedCardActivity::class.java)
                        else -> openFragment()
                    }
                }
            }

            //is available vet button
            if(viewModel.getTable() == "vet"){
                infoActionFrame.frameAvailableCheckBox.apply {
                    visibility = View.VISIBLE

                    setOnClickListener {
                        viewModel.changeVetAvailable(this.isChecked)
                    }
                }
            }
        }
    }

    private fun setupViewModel(){
        //frame data
        viewModel.items.observe(this){
            binding.frames.visibility = View.VISIBLE

            binding.infoTextFrame.frameRecycler.adapter =
                TextAdapter(it.texts)

            binding.infoDataFrame.frameRecycler.adapter =
                SharedDataAdapter(it.sharedData, this)

            it.isAvailableVet?.let { available ->
                binding.infoActionFrame.frameAvailableCheckBox.isChecked = available
            }
        }

        //error message
        viewModel.message.observe(this){
            makeToast(this, it)
        }
    }

    private fun openCreateActivity(activity: Class<*>){
        val openIntent = Intent(this, activity)
        openIntent.putExtra("updateId", viewModel.getId())
        createActivityResult.launch(openIntent)
    }


    private fun openFragment(){
        val args = Bundle().apply {
            putString("table", viewModel.getTable())
            putInt("updateId", viewModel.getId())
            putInt("label", R.string.edit_record)
        }

        val setRecordFragment = SetRecordFragment().apply {
            setCancelable(false)
            arguments = args
        }

        setRecordFragment.show(supportFragmentManager, setRecordFragment.tag)
    }

    override fun openRecordActivity(tableFilter: String) {
        val openIntent = Intent(this, RecordsActivity::class.java).apply {
            putExtra("label", getString(R.string.shared_data))
            putExtra("table", tableFilter)
            putExtra("filter", "id/${viewModel.getTable()}&${viewModel.getId()}")
        }

        startActivity(openIntent)
    }

    override fun update() {
        viewModel.loadItems()
        setResult(RESULT_OK, Intent())
    }
}