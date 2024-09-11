package com.cenkeraydin.wordwave.extension

import android.content.Context
import android.util.Log
import com.cenkeraydin.wordwave.data.Word
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.IOException

class WordList {
    companion object{
        fun loadJSONFromAsset(context: Context): String? {
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
            val jsonString = loadJSONFromAsset(context)

            if (jsonString == null) {
                Log.e("HomeFragment", "JSON dosyası yüklenemedi!")
                return emptyList()  // Eğer JSON null ise boş liste döndürüyoruz
            }
            val gson = Gson()
            // Önce ana JSON nesnesini alıyoruz
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)

            // Sonrasında "words" anahtarındaki diziye ulaşıyoruz
            val wordsJsonArray = jsonObject.getAsJsonArray("words")

            // Son olarak diziyi Word listesine dönüştürüyoruz
            val listType = object : TypeToken<List<Word>>() {}.type
            return gson.fromJson(wordsJsonArray, listType)
        }
    }

}