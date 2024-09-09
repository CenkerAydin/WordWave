package com.cenkeraydin.wordwave.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cenkeraydin.wordwave.Word
import com.cenkeraydin.wordwave.databinding.ItemLayoutWordListBinding

class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemLayoutWordListBinding.bind(itemView)

    fun bind(word: Word) {
        binding.tvWord.text = word.word
        binding.tvMeaning.text = word.definition
    }


}