package com.cenkeraydin.wordwave.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cenkeraydin.wordwave.R
import com.cenkeraydin.wordwave.adapter.LearnedListAdapter
import com.cenkeraydin.wordwave.data.Word
import com.cenkeraydin.wordwave.databinding.FragmentLearnedBinding
import com.cenkeraydin.wordwave.extension.WordList


class LearnedFragment : Fragment() {

    private lateinit var binding: FragmentLearnedBinding
    private lateinit var rvLearnedWordList: RecyclerView
    private lateinit var wordList: ArrayList<Word>
    private lateinit var wordAdapter: LearnedListAdapter
    private lateinit var textView: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLearnedBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterLearnedWords()
        setupRecyclerView()

        setFragmentResultListener("learnedWord") { _, bundle ->
            val learnedWord = bundle.getString("word")
            learnedWord?.let { word ->
                wordList.removeAll { it.english ==word }
                wordAdapter.notifyDataSetChanged()
            }
        }


        setupGradientTextView()
    }

    private fun filterLearnedWords() {
        wordList = WordList.getWordsList(requireContext()) as ArrayList<Word>
        val learnedWords =  loadLearnedWords()
        wordList = wordList.filter { word -> learnedWords.contains(word.english) } as ArrayList<Word>
    }

    private fun loadLearnedWords() : List<String> {
        val sharedPreferences =
            requireActivity().getSharedPreferences("learnedWords", Context.MODE_PRIVATE)
        val learnedWords = sharedPreferences.all.keys.toList()

        return learnedWords
    }

    private fun setupRecyclerView() {
        wordAdapter = LearnedListAdapter(wordList){ selectedWord ->
            val bundle = Bundle()
            bundle.putString("word", selectedWord.english)
            findNavController().navigate(R.id.action_learnedFragment_to_wordPopupLearnedDialog, bundle)
        }

        rvLearnedWordList = binding.rvLearnedWordList
        rvLearnedWordList.adapter = wordAdapter
        rvLearnedWordList.layoutManager = LinearLayoutManager(context)
    }

    private fun setupGradientTextView() {
        textView = binding.tvLearnedWords
        val textShader: Shader = LinearGradient(
            0f, 0f, 0f, textView.textSize,
            intArrayOf(
                resources.getColor(R.color.colorStart, null),
                resources.getColor(R.color.colorEnd, null)
            ), null, Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }


}