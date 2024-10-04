package ua.sviatkuzbyt.vetcliniclapka.ui.info.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityInfoBinding
import ua.sviatkuzbyt.vetcliniclapka.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.info.recyclerviews.TextAdapter

class InfoActivity : AppCompatActivity() {
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

        binding.infoToolBar.setup(getString(viewModel.getLabel()), this)


        viewModel.items.observe(this){
            binding.infoTextFrame.frameRecycler.adapter =
                TextAdapter(it.texts)
        }
    }
}