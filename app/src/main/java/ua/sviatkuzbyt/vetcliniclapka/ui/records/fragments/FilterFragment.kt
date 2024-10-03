package ua.sviatkuzbyt.vetcliniclapka.ui.records.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ua.sviatkuzbyt.vetcliniclapka.databinding.FragmentFilterBinding
import ua.sviatkuzbyt.vetcliniclapka.ui.records.activity.RecordsViewModel
import ua.sviatkuzbyt.vetcliniclapka.ui.records.recycleradapters.FilterAdapter

class FilterFragment : BottomSheetDialogFragment(), FilterAdapter.Action {
    private lateinit var viewModel: RecordsViewModel
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[RecordsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterRecycler.layoutManager = LinearLayoutManager(context)
        binding.filterRecycler.adapter = FilterAdapter(viewModel.getFilterList(), this)
    }

    override fun selectedItem(oldPosition: Int, newPosition: Int) {
        viewModel.updateFilterList(oldPosition, newPosition)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}