package com.example.mydicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mydicodingevent.databinding.FragmentFinishedBinding
import com.example.mydicodingevent.ui.adapter.EventListAdapter
import com.example.mydicodingevent.ui.EventViewModel

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val finishedViewModel: EventViewModel by viewModels()
    private lateinit var eventAdapter: EventListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.rvFinished.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
//        binding.rvFinished.layoutManager = GridLayoutManager(view?.context, 2)

        eventAdapter = EventListAdapter(2)
        binding.rvFinished.apply {
//            layoutManager = LinearLayoutManager(requireContext())
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = eventAdapter
        }
        finishedViewModel.findFinishedEvent()
        finishedViewModel.listFinishedEvents.observe(viewLifecycleOwner){ eventHandler ->
            eventHandler.getContentIfNotHandled()?.let {
                eventAdapter.submitList(it)
            }
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner){ eventHandler ->
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
        finishedViewModel.searchFinishedEvent(query).observe(viewLifecycleOwner) { list ->
            list.let {
                if (list?.isNotEmpty() == true){
                    binding.textView2.visibility = View.GONE
                    binding.rvFinished.visibility = View.VISIBLE
                    eventAdapter.submitList(it)
                } else{
                    binding.rvFinished.visibility = View.INVISIBLE
                    binding.textView2.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}