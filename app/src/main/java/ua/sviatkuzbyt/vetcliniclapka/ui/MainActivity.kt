package ua.sviatkuzbyt.vetcliniclapka.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityMainBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.records.activity.RecordsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setOpenRecordsActivity("pet", btnPets)
            setOpenRecordsActivity("owner", btnOwners)
            setOpenRecordsActivity("vet", btnVets)
            setOpenRecordsActivity("medcard", btnMedHistory)
            setOpenRecordsActivity("appointment", btnAppointments)
        }
    }

    private fun setOpenRecordsActivity(table: String, button: Button){
        button.setOnClickListener {
            val intent = Intent(this, RecordsActivity::class.java).apply {
                putExtra("label", button.text.toString())
                putExtra("table", table)
            }
            startActivity(intent)
        }
    }
}