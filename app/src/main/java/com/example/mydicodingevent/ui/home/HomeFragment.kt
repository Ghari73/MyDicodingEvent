package com.example.mydicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydicodingevent.databinding.FragmentHomeBinding
import com.example.mydicodingevent.ui.adapter.EventListAdapter
import com.example.mydicodingevent.ui.EventViewModel
import com.example.mydicodingevent.ui.adapter.HorizontalListAdapter
import com.example.mydicodingevent.ui.adapter.VerticalListAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: EventViewModel by viewModels()
    private lateinit var horizontalAdapter: HorizontalListAdapter
    private lateinit var verticalAdapter: VerticalListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup rv horizontal
        horizontalAdapter = HorizontalListAdapter()
        binding.rvUpcomingHome.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = horizontalAdapter
        }

        viewModel.findUpcomingEvent()
        viewModel.listUpcomingEvents.observe(viewLifecycleOwner){ eventHandler ->
            eventHandler.getContentIfNotHandled()?.let {
                val limitedList = it.take(5)
                horizontalAdapter.submitList(limitedList)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ eventHandler ->
            eventHandler.getContentIfNotHandled()?.let {
                showLoadingUpcoming(it)
            }
        }

        //setup rv vertical
        verticalAdapter = VerticalListAdapter()
        binding.rvFinishedHome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = verticalAdapter
        }
        viewModel.findFinishedEvent()
        viewModel.listFinishedEvents.observe(viewLifecycleOwner){ eventHandler ->
            eventHandler.getContentIfNotHandled()?.let {
                val limitedList = it.take(5)
                verticalAdapter.submitList(limitedList)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ eventHandler ->
            eventHandler.getContentIfNotHandled()?.let {
                showLoadingFinished(it)
            }
        }
    }

    private fun showLoadingUpcoming(isLoading: Boolean){
        if (isLoading) binding.progressBarUpcoming.visibility = View.VISIBLE
        else binding.progressBarUpcoming.visibility = View.GONE
    }

    private fun showLoadingFinished(isLoading: Boolean){
        if (isLoading) binding.progressBarFinished.visibility = View.VISIBLE
        else binding.progressBarFinished.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}