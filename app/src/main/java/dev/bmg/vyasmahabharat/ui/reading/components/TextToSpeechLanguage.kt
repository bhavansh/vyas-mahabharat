package dev.bmg.vyasmahabharat.ui.reading.components

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

enum class TextToSpeechLanguage {
    ENGLISH,
    SANSKRIT
}

class TextToSpeechManager(context: Context) {
    private var textToSpeech: TextToSpeech? = null
    
    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Initialize with English as default
                val result = textToSpeech?.setLanguage(Locale.US)
                
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported")
                }
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
    }
    
    fun speak(text: String, language: TextToSpeechLanguage) {
        // Set the appropriate language
        when (language) {
            TextToSpeechLanguage.ENGLISH -> {
                textToSpeech?.language = Locale.US
            }
            TextToSpeechLanguage.SANSKRIT -> {
                // Using Hindi as closest available language for Sanskrit
                // For better Sanskrit support, a specialized TTS engine would be needed
                textToSpeech?.language = Locale("hi", "IN")
            }
        }
        
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "verse_tts")
    }
    
    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}