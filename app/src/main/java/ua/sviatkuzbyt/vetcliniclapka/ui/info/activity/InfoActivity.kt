package ua.sviatkuzbyt.vetcliniclapka.ui.info.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityInfoBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.info.recyclerviews.SharedDataAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.info.recyclerviews.TextAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.records.activity.RecordsActivity

class InfoActivity : AppCompatActivity(), SharedDataAdapter.Action {
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

        binding.infoToolBar.setup(getString(viewModel.getLabel()), this)


        viewModel.items.observe(this){
            binding.infoTextFrame.frameRecycler.adapter =
                TextAdapter(it.texts)

            binding.infoDataFrame.frameRecycler.adapter =
                SharedDataAdapter(it.sharedData, this)
        }
    }

    override fun openRecordActivity(table: String, filter: String) {
        val openIntent = Intent(this, RecordsActivity::class.java).apply {
            putExtra("label", getString(R.string.shared_data))
            putExtra("table", table)
            putExtra("filter", filter)
            putExtra("filterId", viewModel.getId())
        }

        startActivity(openIntent)
    }
}