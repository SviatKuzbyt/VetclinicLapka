package ua.sviatkuzbyt.vetcliniclapka.ui.fragments.set

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.FragmentCalendarBinding
import ua.sviatkuzbyt.vetcliniclapka.databinding.FragmentSetRecordBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.activities.records.RecordsViewModel
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.set.SetRecordAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.view.ConfirmCancelWindow


class SetRecordFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSetRecordBinding? = null
    private val binding get() = _binding!!
    private val confirmCancelWindow by lazy { ConfirmCancelWindow(this) }
    private lateinit var viewModel: SetRecordViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetRecordBinding.inflate(inflater, container, false)
        val factory = SetRecordViewModel.Factory(requireActivity().application, "")
        viewModel = ViewModelProvider(this, factory)[SetRecordViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.labelSetRecord.setText(R.string.create_record)
        binding.recyclerSetRecord.layoutManager = LinearLayoutManager(context)

        binding.buttonCancelSetRecord.setOnClickListener {
            confirmCancelWindow.showWindow()
        }

        viewModel.entryItems.observe(viewLifecycleOwner){
            binding.recyclerSetRecord.adapter = SetRecordAdapter(it)
        }

        binding.buttonSetRecord.setOnClickListener {
            binding.recyclerSetRecord.clearFocus()
            viewModel.addData()
        }

        viewModel.message.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            if (it == R.string.added) {
                Log.v("sklt", viewModel.getNewData().toString())
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val MODE_ADD = 1
        const val MODE_EDIT = 2
    }
}