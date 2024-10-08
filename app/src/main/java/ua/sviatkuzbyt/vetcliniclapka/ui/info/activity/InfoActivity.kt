package ua.sviatkuzbyt.vetcliniclapka.ui.info.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityInfoBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.info.recyclerviews.SharedDataAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.info.recyclerviews.TextAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.records.activity.RecordsActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.fragment.SetRecordFragment

class InfoActivity : AppCompatActivity(), SharedDataAdapter.Action,
    SetRecordFragment.SetRecordActions {
    private lateinit var binding: ActivityInfoBinding
    private lateinit var viewModel: InfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = InfoViewModel.Factory(intent)
        viewModel = ViewModelProvider(this, factory)[InfoViewModel::class.java]

        viewModel.message.observe(this){
            makeToast(this, it)
        }

        binding.infoTextFrame.apply {
            frameRecycler.layoutManager = LinearLayoutManager(this@InfoActivity)
            frameLabel.setText(R.string.info)
        }

        binding.infoDataFrame.apply {
            frameRecycler.layoutManager = LinearLayoutManager(this@InfoActivity)
            frameLabel.setText(R.string.shared_data)
        }

        binding.infoActionFrame.apply {
            frameEditButton.setOnClickListener{
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
        }

        binding.infoToolBar.setup(getString(viewModel.getLabel()), this)


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

        if(viewModel.getTable() == "vet"){
            binding.infoActionFrame.frameAvailableCheckBox.apply {
                visibility = View.VISIBLE

                setOnClickListener {
                    viewModel.changeVetAvailable(this.isChecked)
                }
            }
        }
    }

    override fun openRecordActivity(tableFilter: String) {
        val openIntent = Intent(this, RecordsActivity::class.java).apply {
            putExtra("label", getString(R.string.shared_data))
            putExtra("table", tableFilter)
            putExtra("filter", "${viewModel.getTable()}&${viewModel.getId()}")
        }

        startActivity(openIntent)
    }

    override fun add(item: RecordItem) {}

    override fun update() {viewModel.loadItems()}
}