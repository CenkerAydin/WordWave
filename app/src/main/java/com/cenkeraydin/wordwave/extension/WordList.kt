package com.cenkeraydin.wordwave.extension

import android.content.Context
import com.cenkeraydin.wordwave.data.Word
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.Locale

class WordList {
    companion object{
        private fun loadJSONFromAsset(context: Context): String? {
            return try {
                val inputStream = context.assets.open("words.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                null
            }
        }
        fun getWordsList(context: Context): List<Word> {
            val jsonString = loadJSONFromAsset(context) ?: return emptyList()

            val gson = Gson()
            // Önce ana JSON nesnesini alıyoruz
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)

            // Sonrasında "words" anahtarındaki diziye ulaşıyoruz
            val wordsJsonArray = jsonObject.getAsJsonArray("words")

            // Son olarak diziyi Word listesine dönüştürüyoruz
            val listType = object : TypeToken<List<Word>>() {}.type
            val wordsList: List<Word> = gson.fromJson(wordsJsonArray, listType)

            val capitalizedWordsList = wordsList.map { word ->
                Word(
                    word.english.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else it.toString()
                    },
                    word.turkish.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else it.toString()
                    }
                )
            }

            return capitalizedWordsList
        }
    }

}