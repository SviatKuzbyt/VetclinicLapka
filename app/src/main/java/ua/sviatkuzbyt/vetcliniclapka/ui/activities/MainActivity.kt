package ua.sviatkuzbyt.vetcliniclapka.ui.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import androidx.appcompat.app.AppCompatActivity
import ua.sviatkuzbyt.vetcliniclapka.databinding.ActivityMainBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.activities.records.RecordsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPets.setOnClickListener {
            openRecordsActivity("pets")
        }
    }

    private fun openRecordsActivity(table: String){
        val intent = Intent(this, RecordsActivity::class.java)
        intent.putExtra("table", table)
        startActivity(intent)
    }
}