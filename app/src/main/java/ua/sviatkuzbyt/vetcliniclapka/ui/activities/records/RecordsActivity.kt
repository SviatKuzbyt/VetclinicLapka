package ua.sviatkuzbyt.vetcliniclapka.ui.activities.records

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityRecordsBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.RecordAction
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.RecordAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.view.makeErrorToast
import ua.sviatkuzbyt.vetcliniclapka.ui.fragments.records.FilterFragment

class RecordsActivity : AppCompatActivity(), RecordAction {
    private lateinit var binding: ActivityRecordsBinding
    private lateinit var viewModel: RecordsViewModel
    private lateinit var adapterRecycler: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recordsRecycler.layoutManager = LinearLayoutManager(this)

        setToolBar()
        setViewModel()

        binding.buttonSetFilter.setOnClickListener {
            val addFragment = FilterFragment()
            addFragment.show(supportFragmentManager, addFragment.tag)
        }
    }

    private fun setToolBar(){
        val text = intent.getStringExtra("label") ?: "Unknown"
        binding.toolbarFilter.setup(text, this)
    }

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
            }
        }
    }

    override fun clickItem(id: Int) {}
}