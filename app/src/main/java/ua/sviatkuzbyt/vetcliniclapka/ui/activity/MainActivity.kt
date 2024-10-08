package ua.sviatkuzbyt.vetcliniclapka.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityMainBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.appointment.CreateAppointmentActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.create.medcard.CreateMedCardActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.activity.records.RecordsActivity

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

            setOpenCreateActivity(CreateAppointmentActivity::class.java, btnNewAppointment)
            setOpenCreateActivity(CreateMedCardActivity::class.java, btnNewMedcard)
        }
    }

    private fun setOpenRecordsActivity(table: String, button: Button){
        button.setOnClickListener {
            Intent()
            val intent = Intent(this, RecordsActivity::class.java).apply {
                putExtra("label", button.text.toString())
                putExtra("table", table)
            }
            startActivity(intent)
        }
    }

    private fun setOpenCreateActivity(activity: Class<*>, button: Button){
        button.setOnClickListener {
            startActivity(Intent(this, activity))
        }
    }
}