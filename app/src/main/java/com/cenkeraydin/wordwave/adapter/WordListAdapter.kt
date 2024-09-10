package com.cenkeraydin.wordwave.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cenkeraydin.wordwave.R
import com.cenkeraydin.wordwave.data.Word

class WordListAdapter(
    private val wordList: ArrayList<Word>,
    private val onItemClick: (Word) -> Unit
) : RecyclerView.Adapter<WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_word_list, parent, false)
        return WordViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(wordList[position])
        holder.itemView.setOnClickListener {
            onItemClick(wordList[position])
        }
    }
}