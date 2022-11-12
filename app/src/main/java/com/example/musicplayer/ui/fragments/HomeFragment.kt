package com.example.musicplayer.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.adapters.SongAdapter
import com.example.musicplayer.data.other.Status
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.ui.viewmodels.MainViewModel
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var mainViewModel : MainViewModel

    private lateinit var binding : FragmentHomeBinding

    @Inject
    lateinit var songAdapter: SongAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setupRecyclerView()
        subscribeToObservers()
        if (::songAdapter.isInitialized) {
            songAdapter.setOnItemClickListener {
                mainViewModel.playOrToggleSong(it)
            }
        }
    }

    private fun setupRecyclerView() = binding.rvAllSongs.apply {
        if (::songAdapter.isInitialized) {
            adapter = songAdapter
        }
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    binding.allSongsProgressBar.isVisible = false
                    result.data?.let { songs ->
                        if (::songAdapter.isInitialized) {
                            songAdapter.songs = songs
                        }
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> {
                    binding.allSongsProgressBar.isVisible = true
                }

            }
        }
    }
}