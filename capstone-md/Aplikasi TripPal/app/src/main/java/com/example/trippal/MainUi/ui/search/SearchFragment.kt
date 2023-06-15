package com.example.trippal.MainUi.ui.search

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trippal.MainUi.SearchActivity
import com.example.trippal.Preferences.PreferencesManager
import com.example.trippal.databinding.FragmentSearchBinding
import com.google.firebase.firestore.FirebaseFirestore

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private lateinit var sharedPreferences: PreferencesManager
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        sharedPreferences = PreferencesManager(requireContext())
        val username = sharedPreferences.username

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tvUsername.text = username
        binding.searchDestination.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}