package com.cenkeraydin.wordwave.ui


import android.annotation.SuppressLint
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cenkeraydin.wordwave.R
import com.cenkeraydin.wordwave.WordViewModel
import com.cenkeraydin.wordwave.adapter.WordListAdapter
import com.cenkeraydin.wordwave.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var rvWordList: RecyclerView
    private lateinit var wordAdapter: WordListAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var textView: TextView
    private val viewModel: WordViewModel by activityViewModels()

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


        setupRecyclerView()
        setupSwipeRefreshLayout()

        viewModel.wordList.observe(viewLifecycleOwner) { words ->
            wordAdapter.submitList(words)
        }

        setFragmentResultListener("learnedWord") { _, bundle ->
            val learnedWord = bundle.getString("word")
            learnedWord?.let { word ->
               viewModel.removeLearnedWord(word)
            }
        }

        setupGradientText()
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout = binding.swipeRefresh
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.shuffleWords()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        wordAdapter = WordListAdapter { selectedWord ->
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

    private fun setupGradientText() {
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

