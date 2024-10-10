package ua.sviatkuzbyt.vetcliniclapka.ui.fragments.time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.databinding.FragmentCalendarBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private var selectedDate = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val monthStr = if(month > 8) "${month+1}"
            else "0${month+1}"

            selectedDate = "$year-$monthStr-$dayOfMonth"
        }

        binding.buttonCalendar.setOnClickListener {
            val result = Bundle()
            result.putString("date", selectedDate.ifBlank { currentDay() })
            parentFragmentManager.setFragmentResult("dateFr", result)
            dismiss()
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