package ua.sviatkuzbyt.vetcliniclapka.ui.activity.records

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityRecordsBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment.CreateAppointmentActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.recyclers.record.RecordAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.hideKeyboard
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.info.InfoActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.medcard.CreateMedCardActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.time.CalendarFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.record.FilterFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.setdata.SetRecordFragment

class RecordsActivity :
    AppCompatActivity(),
    RecordAdapter.Action,
    SetRecordFragment.SetRecordActions
{
    private lateinit var binding: ActivityRecordsBinding
    private lateinit var viewModel: RecordsViewModel
    private lateinit var adapterRecycler: RecordAdapter

    private val createActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
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

    private val infoActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            viewModel.reload()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewModel()
        setViews()

        supportFragmentManager.setFragmentResultListener("dateFr", this){ _, bundle ->
            val date = bundle.getString("date") ?: "2024-01-01"
            binding.filterText.setText(date)
            viewModel.getFilterData(date)
        }
    }

    private fun setViews(){
        //RecyclerView
        binding.recordsRecycler.layoutManager = LinearLayoutManager(this)

        //EditText
        binding.filterText.setOnEditorActionListener { view, _, _ ->
            val filter = view.text.toString()

            if (filter.isBlank())
                makeToast(this, R.string.empty_field)
            else{
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

    private fun openActivity(activity: Class<*>){
        val createIntent = Intent(this, activity)
        createIntent.putExtra("return", true)
        createActivityResult.launch(createIntent)
    }

    private fun showFragment(fragment: BottomSheetDialogFragment){
        fragment.show(supportFragmentManager, fragment.tag)
    }

    private fun setToolBar(){
        val text = viewModel.getLabel()
        binding.toolbarFilter.setup(text, this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewModel() {
        //Init viewModel
        val factory = RecordsViewModel.Factory(intent)
        viewModel = ViewModelProvider(this, factory)[RecordsViewModel::class.java]

        viewModel.message.observe(this) {
            makeToast(this, it)
        }

        //Records list
        viewModel.records.observe(this) {
            if (!::adapterRecycler.isInitialized) {
                adapterRecycler = RecordAdapter(it, this, viewModel.getIcon())
                binding.recordsRecycler.adapter = adapterRecycler
            } else
                adapterRecycler.notifyDataSetChanged()
        }

        //CalendarButton
        viewModel.showCalendarButton.observe(this){
            binding.buttonChooseDate.apply {
                if (it && isGone) visibility = View.VISIBLE
                else if(isVisible) visibility = View.GONE
            }
        }
    }

    //RecordAction interface
    override fun clickItem(item: RecordItem) {
        when(viewModel.getMode()){
            ACTION_VIEW -> openRecord(item)
            ACTION_SELECT -> returnRecord(item)
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
        } catch (_: Exception){
            makeToast(this, R.string.reload_page)
        }
    }

    override fun update() {}

    companion object{
        const val ACTION_VIEW = 1
        const val ACTION_SELECT = 2
    }
}