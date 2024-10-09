package ua.sviatkuzbyt.vetcliniclapka.ui.setdata.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.R
import ua.sviatkuzbyt.vetcliniclapka.data.RecordItem
import ua.sviatkuzbyt.vetcliniclapka.data.SetRecordItem
import ua.sviatkuzbyt.vetcliniclapka.databinding.FragmentSetRecordBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.makeToast
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.SetRecordAdapter
import ua.sviatkuzbyt.vetcliniclapka.ui.elements.views.ConfirmCancelWindow
import ua.sviatkuzbyt.vetcliniclapka.ui.records.activity.RecordsActivity
import ua.sviatkuzbyt.vetcliniclapka.ui.setdata.recycleradapter.holders.SelectViewHolder

class SetRecordFragment :
    BottomSheetDialogFragment(),
    SelectViewHolder.Action
{
    private var _binding: FragmentSetRecordBinding? = null
    private val binding get() = _binding!!
    private val confirmCancelWindow by lazy { ConfirmCancelWindow(this) }
    private lateinit var viewModel: SetRecordViewModel
    private lateinit var adapter: SetRecordAdapter

    private val selectActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == AppCompatActivity.RESULT_OK){
            val resultData = it.data

            resultData?.let { data ->
                viewModel.updateSelectItem(
                    data.getStringExtra("label") ?: "Unknown",
                    data.getIntExtra("forPosition", 0),
                    data.getIntExtra("id", 0).toString()
                )
            }
        }
    }

    interface SetRecordActions{
        fun add(item: RecordItem)
        fun update()
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

        val factory = SetRecordViewModel.Factory(requireArguments())
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
            val updatePosition = viewModel.getUpdatePosition()
            if (updatePosition == SetRecordViewModel.NO_UPDATE_POSITION)
                setRecyclerAdapter(it)
            else
                updateRecyclerAdapter(it, updatePosition)
        }

        //complete or error
        viewModel.message.observe(viewLifecycleOwner){
            makeToast(requireContext(), it)
            when (it) {
                R.string.added -> {
                    viewModel.getNewData()?.let { item -> action?.add(item) }
                    dismiss()
                }
                R.string.edited -> {
                    action?.update()
                    dismiss()
                }
                else -> binding.buttonSetRecord.isEnabled = true
            }
        }
    }

    private fun setRecyclerAdapter(list: List<SetRecordItem>){
        adapter = SetRecordAdapter(list, this)
        binding.recyclerSetRecord.adapter = adapter
    }

    private fun updateRecyclerAdapter(list: List<SetRecordItem>, position: Int){
        try {
            adapter.notifyItemChanged(position)
        } catch (e: Exception){
            setRecyclerAdapter(list)
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

        binding.labelSetRecord.setText(viewModel.getLabel())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun select(position: Int, item: SetRecordItem) {
        val selectIntent = Intent(requireActivity(), RecordsActivity::class.java).apply {
            putExtra("table", item.apiName)
            putExtra("label", getString(R.string.select_recor))
            putExtra("openMode", RecordsActivity.ACTION_SELECT)
            putExtra("forPosition", position)
        }
        Log.v("sklt", item.apiName)
        selectActivityResult.launch(selectIntent)
    }
}