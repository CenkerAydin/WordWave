package com.cenkeraydin.wordwave

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cenkeraydin.wordwave.data.Word
import com.cenkeraydin.wordwave.extension.WordList

class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val _wordList = MutableLiveData<List<Word>>()
    val wordList: LiveData<List<Word>> get() = _wordList
    init {
        initializeWordList()
    }


    private fun removeLearnedItemsFromWordList(initalizeList: List<Word>) : List<Word>{
        val learnedWords = getApplication<Application>().getSharedPreferences("learnedWords", Context.MODE_PRIVATE)
        val learnedWordsList = learnedWords.all.keys.toList()
        val currentList = initalizeList.toMutableList()
        currentList.removeAll { it.english in learnedWordsList }
        return currentList
    }

    private fun initializeWordList() {
        val context = getApplication<Application>().applicationContext
        var initializeList = WordList.getWordsList(context) as ArrayList<Word>
        _wordList.value = removeLearnedItemsFromWordList(initializeList)
    }

    fun shuffleWords() {
        val currentList = _wordList.value ?: return
        val shuffledList = currentList.shuffled()
        _wordList.value = shuffledList
    }

    fun removeLearnedWord(word: String) {
        val currentList = _wordList.value?.toMutableList() ?: return
        currentList.removeAll { it.english == word }
        _wordList.value = currentList
    }
}
