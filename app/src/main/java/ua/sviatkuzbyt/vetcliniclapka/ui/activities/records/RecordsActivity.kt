package ua.sviatkuzbyt.vetcliniclapka.ui.activities.records

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityRecordsBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.RecordAction
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.RecordAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.view.hideKeyboard
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.view.makeErrorToast
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.records.CalendarFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.records.FilterFragment
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.set.SetRecordFragment

class RecordsActivity : AppCompatActivity(), RecordAction, CalendarFragment.CalendarFilterAction {
    private lateinit var binding: ActivityRecordsBinding
    private lateinit var viewModel: RecordsViewModel
    private lateinit var adapterRecycler: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewModel()


        binding.recordsRecycler.layoutManager = LinearLayoutManager(this)

        setToolBar()

        binding.buttonSetFilter.setOnClickListener {
            val addFragment = FilterFragment()
            addFragment.show(supportFragmentManager, addFragment.tag)
        }

        binding.filterText.setOnEditorActionListener { view, _, _ ->
            val filter = view.text.toString()
            if (filter.isBlank())
                Toast.makeText(this, R.string.empty_field, Toast.LENGTH_LONG).show()
            else{
                viewModel.getFilterData(view.text.toString())
                hideKeyboard(view)
            }
            true
        }

        binding.buttonChooseDate.setOnClickListener {
            val calendarFragment = CalendarFragment()
            calendarFragment.show(supportFragmentManager, calendarFragment.tag)
        }

        binding.buttonCreate.setOnClickListener {
            val setRecordFragment = SetRecordFragment()
            setRecordFragment.setCancelable(false)
            val args = Bundle().apply {
                putString("table", viewModel.getTable())
                putInt("mode", SetRecordFragment.MODE_ADD)
            }
            setRecordFragment.arguments = args
            setRecordFragment.show(supportFragmentManager, setRecordFragment.tag)
        }
    }

    private fun setToolBar(){
        val text = viewModel.getLabel()
        binding.toolbarFilter.setup(text, this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewModel() {
        val factory = RecordsViewModel.Factory(application, intent)
        viewModel = ViewModelProvider(this, factory)[RecordsViewModel::class.java]

        viewModel.message.observe(this) {
            makeErrorToast(it, this)
        }

        viewModel.records.observe(this) {
            if (!::adapterRecycler.isInitialized) {
                adapterRecycler = RecordAdapter(it, this, viewModel.getIcon())
                binding.recordsRecycler.adapter = adapterRecycler
            } else{
                adapterRecycler.notifyDataSetChanged()
            }
        }

        viewModel.showCalendarButton.observe(this){
            binding.buttonChooseDate.apply {
                if (it && isGone) visibility = View.VISIBLE
                else if(isVisible) visibility = View.GONE
            }
        }
    }

    override fun clickItem(id: Int) {}

    override fun search(date: String) {
        binding.filterText.setText(date)
        viewModel.getFilterData(date)
    }
}