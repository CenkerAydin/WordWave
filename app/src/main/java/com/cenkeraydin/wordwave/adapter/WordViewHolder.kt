package com.cenkeraydin.wordwave.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cenkeraydin.wordwave.data.Word
import com.cenkeraydin.wordwave.databinding.ItemLayoutWordListBinding

class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemLayoutWordListBinding.bind(itemView)

    fun bind(word: Word) {
        binding.tvWord.text = word.english
        binding.tvMeaning.text = word.turkish
    }


}