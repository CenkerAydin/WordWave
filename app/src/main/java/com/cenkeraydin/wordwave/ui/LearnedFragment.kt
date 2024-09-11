package com.cenkeraydin.wordwave.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLearnedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterLearnedWords()
        wordAdapter = LearnedListAdapter(wordList){ selectedWord ->
            val bundle = Bundle()
            bundle.putString("word", selectedWord.english)
            findNavController().navigate(R.id.action_learnedFragment_to_wordPopupLearnedDialog, bundle)
        }

        setFragmentResultListener("learnedWord") { _, bundle ->
            val learnedWord = bundle.getString("word")
            learnedWord?.let { word ->
                wordList.removeAll { it.english ==word }
                wordAdapter.notifyDataSetChanged()
            }
        }

        rvLearnedWordList = binding.rvLearnedWordList
        rvLearnedWordList.adapter = wordAdapter
        rvLearnedWordList.layoutManager = LinearLayoutManager(context)


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


}