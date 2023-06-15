package com.example.trippal.MainUi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trippal.databinding.FragmentAlamBinding

class ReligiFragment : Fragment() {
    private var _binding: FragmentAlamBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlamBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun create(): ReligiFragment {
            return ReligiFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
