package com.cenkeraydin.wordwave.ui


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.cenkeraydin.wordwave.extension.WordList


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var rvWordList: RecyclerView
    private lateinit var wordList: ArrayList<Word>
    private lateinit var wordAdapter: WordListAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var lastOrder: List<String> = emptyList()
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeWordList()
        filterLearnedWords()
        setupRecyclerView()

        setFragmentResultListener("learnedWord") { _, bundle ->
            val learnedWord = bundle.getString("word")
            learnedWord?.let { word ->
                wordList.removeAll { it.english ==word }
                wordAdapter.notifyDataSetChanged()
            }
        }

        swipeRefreshLayout = binding.swipeRefresh
        swipeRefreshLayout.setOnRefreshListener {
            shuffleWordList()
            swipeRefreshLayout.isRefreshing = false
        }

        setupGradientText()

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun shuffleWordList() {
        var newOrder: List<String>
        do {
            newOrder = wordList.map { it.english }.shuffled()
        } while (newOrder == lastOrder)
        lastOrder = newOrder
        wordList.sortBy { newOrder.indexOf(it.english) }
        wordAdapter.notifyDataSetChanged()
    }

    private fun initializeWordList() {
        val context = requireContext()
        wordList = WordList.getWordsList(context) as ArrayList<Word>
    }

    private fun filterLearnedWords() {
        val learnedWords = getLearnedWords()
        wordList = wordList.filter { word -> !learnedWords.contains(word.english) } as ArrayList<Word>
    }

    private fun getLearnedWords(): List<String> {
        val sharedPreferences = requireContext().getSharedPreferences("learnedWords", Context.MODE_PRIVATE)
        return sharedPreferences.all.keys.toList()
    }

    private fun setupRecyclerView() {
        wordAdapter = WordListAdapter(wordList) { selectedWord ->
            val bundle = Bundle().apply {
                putString("word", selectedWord.english)
                putString("meaning", selectedWord.turkish)
            }
            findNavController().navigate(R.id.wordPopupDialog, bundle)
        }

        rvWordList = binding.rvWordList
        rvWordList.adapter = wordAdapter
        rvWordList.layoutManager = LinearLayoutManager(context)
    }

    private fun setupGradientText(){
        textView = binding.tvAppName
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