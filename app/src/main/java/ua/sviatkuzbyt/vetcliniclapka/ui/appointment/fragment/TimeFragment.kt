package ua.sviatkuzbyt.vetcliniclapka.ui.appointment.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.databinding.FragmentTimeBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTimeBinding? = null
    private val binding get() = _binding!!
    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val monthStr = if(month > 8) "${month+1}"
            else "0${month+1}"
            selectedDate = "$year-$monthStr-$dayOfMonth"
        }

        binding.time.setIs24HourView(true)

        binding.buttonCalendar.setOnClickListener {
            if (binding.calendar.isVisible){
                binding.calendar.visibility = View.GONE
                binding.timeCardView.visibility = View.VISIBLE
                if (selectedDate.isBlank()) selectedDate = currentDay()
            }

            else{
                selectedTime = "${binding.time.hour}:${binding.time.minute}:00"
                val result = Bundle()
                result.putString("time", "$selectedDate%20$selectedTime")
                result.putString("timeLabel", "$selectedDate $selectedTime")
                parentFragmentManager.setFragmentResult("timeFr", result)
                dismiss()
            }
        }
    }

    private fun currentDay(): String {
        return try {
            val date = Date(binding.calendar.date)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            format.format(date)
        } catch (_: Exception){
            makeToast(requireContext(), R.string.error)
            ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}