package com.example.s164270lykkehjulet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.s164270lykkehjulet.databinding.FragmentGameLostBinding

/**
 * A simple [Fragment] subclass.
 * Use the [GameLostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameLostFragment : Fragment() {

    private var _binding: FragmentGameLostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameLostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goBackButton.setOnClickListener {
            val action = GameLostFragmentDirections.actionGameLostFragmentToGuessingFragment()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}