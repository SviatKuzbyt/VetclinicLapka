package ua.sviatkuzbyt.vetcliniclapka.ui.activity.records

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.ConstState
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityRecordsBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment.CreateAppointmentActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.recyclers.record.RecordAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.hideKeyboard
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.info.InfoActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.medcard.CreateMedCardActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.report.ReportActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.time.CalendarFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.record.FilterFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.setdata.SetRecordFragment

class RecordsActivity :
    AppCompatActivity(),
    RecordAdapter.Action,
    SetRecordFragment.SetRecordActions
{
    private lateinit var binding: ActivityRecordsBinding
    private val viewModel: RecordsViewModel by viewModels {
        RecordsViewModel.Factory(intent)
    }
    private lateinit var adapterRecycler: RecordAdapter

    //add new appointment or medcard to list
    private val createActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                it.data?.let { data ->
                    val recordItem = RecordItem(
                        data.getIntExtra("id", 0),
                        data.getStringExtra("label") ?: "Unknown",
                        data.getStringExtra("subtext") ?: ""
                    )
                    add(recordItem)
                }
            }
        }

    //apply item change form InfoActivity
    private val infoActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                viewModel.reload()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViews()
        setViewModel()
    }

    private fun setViews(){
        //RecyclerView
        binding.recordsRecycler.layoutManager = LinearLayoutManager(this)
        adapterRecycler = RecordAdapter(this, viewModel.getIcon())
        binding.recordsRecycler.adapter = adapterRecycler

        //EditText
        binding.filterText.setOnEditorActionListener { view, _, _ ->
            val filter = view.text.toString()

            if (filter.isBlank()){
                makeToast(this, R.string.empty_field)
            } else{
                viewModel.getFilterData(filter)
                hideKeyboard(view)
            }
            true
        }

        //EditText's buttons
        binding.buttonSetFilter.setOnClickListener {
            showFragment(FilterFragment())
        }

        binding.buttonChooseDate.setOnClickListener {
            showFragment(CalendarFragment())
        }

        //Add button
        binding.buttonCreate.setOnClickListener {
            when(viewModel.getTable()){
                "appointment" -> openActivity(CreateAppointmentActivity::class.java)
                "medcard" -> openActivity(CreateMedCardActivity::class.java)
                else -> openFragment()
            }
        }

        //ToolBar
        setToolBar()

        //calendar pick result
        supportFragmentManager.setFragmentResultListener("dateFr", this){ _, bundle ->
            val date = bundle.getString("date") ?: "2024-01-01"
            binding.filterText.setText(date)
            viewModel.getFilterData(date)
        }

        offButtonsForBreed()
    }

    private fun openActivity(activity: Class<*>){
        val createIntent = Intent(this, activity)
        createIntent.putExtra("return", true)
        createActivityResult.launch(createIntent)
    }

    private fun openFragment(){
        val args = Bundle().apply {
            putString("table", viewModel.getTable())
            putInt("label", R.string.create_record)
        }

        val setRecordFragment = SetRecordFragment().apply {
            setCancelable(false)
            arguments = args
        }

        showFragment(setRecordFragment)
    }

    private fun showFragment(fragment: BottomSheetDialogFragment){
        fragment.show(supportFragmentManager, fragment.tag)
    }

    private fun setToolBar(){
        val text = viewModel.getLabel()
        binding.toolbarFilter.setup(
            text, this, R.drawable.ic_report
        ) {
            openReportActivity()
        }
    }

    private fun openReportActivity(){
        val reportIntent = Intent(this, ReportActivity::class.java).apply {
            putExtra("table", viewModel.getLabel())
            putExtra("tableApi", viewModel.getTable())
            putExtra("filter", getString(viewModel.getSelectedFilter()))
            putExtra("filterApi", viewModel.getSelectedFilterApi())
            putExtra("filterKey", binding.filterText.text.toString())
        }

        startActivity(reportIntent)
    }

    private fun offButtonsForBreed(){
        if (viewModel.getTable() == "breed"){
            binding.buttonCreate.visibility = View.GONE
            binding.toolbarFilter.hideActionButton()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewModel() {
        //Records list
        viewModel.records.observe(this) {
            adapterRecycler.addAll(it)
        }

        //CalendarButton
        viewModel.showCalendarButton.observe(this){
            binding.buttonChooseDate.apply {
                if (it && isGone) visibility = View.VISIBLE
                else if(isVisible) visibility = View.GONE
            }
        }

        viewModel.message.observe(this) {
            makeToast(this, it)
        }
    }

    //RecordAction interface
    override fun clickItem(item: RecordItem) {
        when(viewModel.getMode()){
            ConstState.RECORD_ACTION_VIEW -> openRecord(item)
            ConstState.RECORD_ACTION_SELECT -> returnRecord(item)
        }
    }

    private fun openRecord(item: RecordItem){
        val openIntent = Intent(this, InfoActivity::class.java). apply {
            putExtra("id", item.id)
            putExtra("table", viewModel.getTable())
        }
        infoActivityResult.launch(openIntent)
    }

    private fun returnRecord(item: RecordItem){
        val resultData = Intent().apply {
            putExtra("id", item.id)
            putExtra("label", item.label)
            putExtra("forPosition", intent.getIntExtra("forPosition", 0))
        }

        setResult(RESULT_OK, resultData)
        finish()
    }

    //SetRecordActions interface
    override fun add(item: RecordItem) {
        try {
            adapterRecycler.add(item)
            binding.recordsRecycler.scrollToPosition(0)
            viewModel.add(item)
        } catch (_: Exception){
            makeToast(this, R.string.reload_page)
        }
    }

    override fun update() {}
}