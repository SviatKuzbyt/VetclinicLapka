package ua.sviatkuzbyt.vetcliniclapka.ui.activity.report

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityReportBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.recyclers.ReportAdapter

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.report.observe(this){
            binding.reportList.adapter = ReportAdapter(this, it)
        }

        viewModel.message.observe(this){
            makeToast(this, it)
        }
    }
}