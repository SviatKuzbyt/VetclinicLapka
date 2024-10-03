package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.record.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.FragmentSetRecordBinding
import ua.sviatkuzbyt.vetcliniclapka.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.SetRecordAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.views.ConfirmCancelWindow

class SetRecordFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSetRecordBinding? = null
    private val binding get() = _binding!!
    private val confirmCancelWindow by lazy { ConfirmCancelWindow(this) }
    private lateinit var viewModel: SetRecordViewModel

    interface SetRecordActions{
        fun add(item: RecordItem)
    }

    private var action: SetRecordActions? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is SetRecordActions) action = context
        else{
            makeToast(context, R.string.cantAdd)
            dismiss()
        }
    }

    //init
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetRecordBinding.inflate(inflater, container, false)

        val factory = SetRecordViewModel.Factory(
            requireActivity().application,
            arguments?.getString("table")
        )
        viewModel = ViewModelProvider(this, factory)[SetRecordViewModel::class.java]
        return binding.root
    }

    //set
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setViews()
    }

    private fun setViewModel(){
        //list
        viewModel.entryItems.observe(viewLifecycleOwner){
            binding.recyclerSetRecord.adapter = SetRecordAdapter(it)
        }

        //complete or error
        viewModel.message.observe(viewLifecycleOwner){
            makeToast(requireContext(), it)
            if (it == R.string.added) {
                viewModel.getNewData()?.let { item -> action?.add(item) }
                dismiss()
            } else binding.buttonSetRecord.isEnabled = true
        }
    }

    private fun setViews(){
        //label and list
        binding.labelSetRecord.setText(R.string.create_record)
        binding.recyclerSetRecord.layoutManager = LinearLayoutManager(context)

        //buttons
        binding.buttonCancelSetRecord.setOnClickListener {
            confirmCancelWindow.showWindow()
        }

        binding.buttonSetRecord.setOnClickListener {
            binding.buttonSetRecord.isEnabled = false
            viewModel.addData()
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