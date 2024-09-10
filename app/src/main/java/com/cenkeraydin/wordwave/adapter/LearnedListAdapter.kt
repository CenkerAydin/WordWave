package com.cenkeraydin.wordwave.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cenkeraydin.wordwave.R
import com.cenkeraydin.wordwave.data.Word

class LearnedListAdapter(
    private val learnedWordList: List<Word>,
    private val onItemClick: (Word) -> Unit
) : RecyclerView.Adapter<WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_word_list, parent, false)
        return WordViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return learnedWordList.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(learnedWordList[position])
        holder.itemView.setOnClickListener {
            onItemClick(learnedWordList[position])
        }
    }
}