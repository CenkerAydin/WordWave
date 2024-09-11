package com.cenkeraydin.wordwave.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cenkeraydin.wordwave.R
import com.cenkeraydin.wordwave.adapter.WordListAdapter
import com.cenkeraydin.wordwave.data.Word
import com.cenkeraydin.wordwave.databinding.FragmentHomeBinding
import kotlin.random.Random


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var rvWordList: RecyclerView
    private lateinit var wordList: ArrayList<Word>
    private lateinit var wordAdapter: WordListAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var lastOrder: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeWordList()
        filterLearnedWords()
        setupRecyclerView()

        setFragmentResultListener("learnedWord") { _, bundle ->
            val learnedWord = bundle.getString("word")
            learnedWord?.let { word ->
                wordList.removeAll { it.word ==word }
                wordAdapter.notifyDataSetChanged()
            }
        }

        swipeRefreshLayout = binding.swipeRefresh
        swipeRefreshLayout.setOnRefreshListener {
            shuffleWordList()
            swipeRefreshLayout.isRefreshing = false
        }




    }


    private fun shuffleWordList() {
        var newOrder: List<String>
        do {
            newOrder = wordList.map { it.word }.shuffled()
        } while (newOrder == lastOrder)
        lastOrder = newOrder
        wordList.sortBy { newOrder.indexOf(it.word) }
        wordAdapter.notifyDataSetChanged()
    }

    private fun initializeWordList() {
        wordList = arrayListOf(
            Word("Learn", "Öğrenmek", false),
            Word("banana", "muz", false),
            Word("cat", "kedi", false),
            Word("de", "kedi", false),
            Word("e", "kedi", false),
            Word("c", "kedi", false)

        )
    }
    private fun filterLearnedWords() {
        val learnedWords = getLearnedWords()
        wordList = wordList.filter { word -> !learnedWords.contains(word.word) } as ArrayList<Word>
    }

    private fun getLearnedWords(): List<String> {
        val sharedPreferences = requireContext().getSharedPreferences("learnedWords", Context.MODE_PRIVATE)
        return sharedPreferences.all.keys.toList()
    }

    private fun setupRecyclerView() {
        wordAdapter = WordListAdapter(wordList) { selectedWord ->
            val bundle = Bundle().apply {
                putString("word", selectedWord.word)
                putString("meaning", selectedWord.definition)
            }
            findNavController().navigate(R.id.wordPopupDialog, bundle)
        }

        rvWordList = binding.rvWordList
        rvWordList.adapter = wordAdapter
        rvWordList.layoutManager = LinearLayoutManager(context)
    }



}