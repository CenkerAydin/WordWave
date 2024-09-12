package com.cenkeraydin.wordwave.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.cenkeraydin.wordwave.R
import com.cenkeraydin.wordwave.databinding.FragmentPopUpBinding

class PopupUnlearnedFragment : DialogFragment() {

    private lateinit var binding: FragmentPopUpBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentPopUpBinding.inflate(layoutInflater)

        val word = arguments?.getString("word") ?: ""

        binding.textViewWord.text = word

        binding.buttonLearned.setOnClickListener {
            saveWordToSharedPreferences(word)
            startAnimationAndDismiss(word)
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setNegativeButton("Cancel") { _, _ -> dismiss() }
            .create()
    }

    private fun startAnimationAndDismiss(word: String) {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_right)
        binding.root.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // No action needed
            }

            override fun onAnimationEnd(animation: Animation?) {
                setFragmentResult("learnedWord", Bundle().apply {
                    putString("word", word)
                })
                dismiss()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // No action needed
            }
        })
    }

    private fun saveWordToSharedPreferences(word: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("learnedWords", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(word,word)
        editor.apply()

        Toast.makeText(requireContext(), "$word saved as learned", Toast.LENGTH_SHORT).show()
    }


}

