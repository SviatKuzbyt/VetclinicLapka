package ua.sviatkuzbyt.vetcliniclapka.ui.records.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityRecordsBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.records.recycleradapters.RecordAdapter
import ua.sviatkuzbyt.vetcliniclapka.hideKeyboard
import ua.sviatkuzbyt.vetcliniclapka.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.records.fragments.CalendarFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.records.fragments.FilterFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.fragment.SetRecordFragment

class RecordsActivity :
    AppCompatActivity(),
    RecordAdapter.Action,
    CalendarFragment.CalendarFilterAction,
    SetRecordFragment.SetRecordActions
{
    private lateinit var binding: ActivityRecordsBinding
    private lateinit var viewModel: RecordsViewModel
    private lateinit var adapterRecycler: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewModel()
        setViews()
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
            val args = Bundle().apply {
                putString("table", viewModel.getTable())
                putInt("mode", SetRecordFragment.MODE_ADD)
            }

            val setRecordFragment = SetRecordFragment().apply {
                setCancelable(false)
                arguments = args
            }

            showFragment(setRecordFragment)
        }

        //ToolBar
        setToolBar()
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
        val factory = RecordsViewModel.Factory(application, intent)
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
        val resultData = Intent().apply {
            putExtra("id", item.id)
            putExtra("label", item.label)
            putExtra("forPosition", intent.getIntExtra("forPosition", 0))
        }
        setResult(RESULT_OK, resultData)
        finish()
    }

    //CalendarFilterAction interface
    override fun search(date: String) {
        binding.filterText.setText(date)
        viewModel.getFilterData(date)
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

    companion object{
        const val ACTION_VIEW = 1
        const val ACTION_SELECT = 2
    }
}