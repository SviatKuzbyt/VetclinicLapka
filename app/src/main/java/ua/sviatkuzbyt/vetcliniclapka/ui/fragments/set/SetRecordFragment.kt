package ua.sviatkuzbyt.vetcliniclapka.ui.fragments.set

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.FragmentCalendarBinding
import ua.sviatkuzbyt.vetcliniclapka.databinding.FragmentSetRecordBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.recycleradapters.set.SetRecordAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.view.ConfirmCancelWindow


class SetRecordFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSetRecordBinding? = null
    private val binding get() = _binding!!
    private val confirmCancelWindow by lazy { ConfirmCancelWindow(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.labelSetRecord.setText(R.string.create_record)
        binding.recyclerSetRecord.layoutManager = LinearLayoutManager(context)

        binding.buttonCancelSetRecord.setOnClickListener {
            confirmCancelWindow.showWindow()
        }

        //START TEST
        val tempList = listOf(
            SetRecordItem(label=R.string.name, apiName="name"),
            SetRecordItem(label=R.string.phone, apiName="phone")
        )

        binding.recyclerSetRecord.adapter = SetRecordAdapter(tempList)

        binding.buttonSetRecord.setOnClickListener {
            binding.recyclerSetRecord.clearFocus()
            Log.v("sklt", tempList.toString())
            dismiss()
        }
        //END TEST
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}