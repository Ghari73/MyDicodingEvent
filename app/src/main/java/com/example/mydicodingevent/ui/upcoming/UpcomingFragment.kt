package com.example.mydicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydicodingevent.databinding.FragmentUpcomingBinding
import com.example.mydicodingevent.ui.adapter.EventListAdapter
import com.example.mydicodingevent.ui.EventViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val upcomingViewModel: EventViewModel by viewModels()
    private lateinit var eventAdapter: EventListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventListAdapter(1)
        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = eventAdapter
        }

        upcomingViewModel.findUpcomingEvent()
        upcomingViewModel.listUpcomingEvents.observe(viewLifecycleOwner){ eventHandler ->
            eventHandler.getContentIfNotHandled()?.let {
                eventAdapter.submitList(it)
            }
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner){ eventHandler ->
            eventHandler.getContentIfNotHandled()?.let {
                showLoading(it)
            }
        }

        setupSearchBar()
    }
    private fun setupSearchBar() {

        val searchView = binding.searchView2

        // Setel query hint (teks yang muncul saat search view kosong)
        searchView.queryHint = "Cari Event"

        // Implementasikan listener untuk menghandle perubahan query pada search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Panggil fungsi untuk melakukan pencarian berdasarkan query
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Panggil fungsi untuk melakukan pencarian berdasarkan teks yang berubah
                performSearch(newText)
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        // Lakukan pencarian berdasarkan query di view model
        upcomingViewModel.searchUpcomingEvent(query).observe(viewLifecycleOwner) { list ->
            list.let {
                if (list?.isNotEmpty() == true){
                    binding.textView2.visibility = View.GONE
                    binding.rvUpcoming.visibility = View.VISIBLE
                    eventAdapter.submitList(it)
                } else{
                    binding.rvUpcoming.visibility = View.INVISIBLE
                    binding.textView2.visibility = View.VISIBLE
                }
            }
        }
    }
//    private fun setUpcomingEventList(eventList: List<ListEventsItem>){
//        val adapter = EventListAdapter(0)
//        adapter.submitList(eventList)
//        binding.rvUpcoming.adapter = adapter
//    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}