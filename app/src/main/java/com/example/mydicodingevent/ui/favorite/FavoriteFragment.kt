package com.example.mydicodingevent.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mydicodingevent.R
import com.example.mydicodingevent.databinding.FragmentFavoriteBinding
import com.example.mydicodingevent.databinding.FragmentFinishedBinding
import com.example.mydicodingevent.ui.EventViewModel
import com.example.mydicodingevent.ui.ViewModelFactory
import com.example.mydicodingevent.ui.adapter.EventListAdapter

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val favViewModel: EventViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var eventAdapter: EventListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

}