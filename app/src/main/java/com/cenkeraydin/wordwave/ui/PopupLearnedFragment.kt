package com.cenkeraydin.wordwave.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.cenkeraydin.wordwave.databinding.FragmentPopupLearnedBinding


class PopupLearnedFragment : DialogFragment() {

    private lateinit var binding: FragmentPopupLearnedBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentPopupLearnedBinding.inflate(layoutInflater)

        val word = arguments?.getString("word") ?: ""

        binding.textViewWord.text = word

        binding.buttonUnlearned.setOnClickListener() {
            removeWordFromSharedPreferences(word)
            setFragmentResult("learnedWord", Bundle().apply {
                putString("word", word)
            })
            dismiss()
        }


        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setNegativeButton("Cancel") { _, _ -> dismiss() }
            .create()
    }

    private fun removeWordFromSharedPreferences(word: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("learnedWords", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(word)
        editor.apply()

        Toast.makeText(requireContext(), "$word saved as unlearned", Toast.LENGTH_SHORT).show()

    }
}