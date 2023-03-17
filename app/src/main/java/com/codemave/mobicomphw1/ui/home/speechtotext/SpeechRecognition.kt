package com.codemave.mobicomphw1.ui.home.speechtotext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

fun startSpeechToText(
    vm: SpeechToTextViewModel,
    ctx: Context,
    //finished: ()-> Unit,
    //vm: ViewModel = SpeechtToTextViewModel = viewModel()
) {
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ctx)
    val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    speechRecognizerIntent.putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
    )

    // Optionally I have added my mother language
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el_GR")

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(v: Float) {}
        override fun onBufferReceived(bytes: ByteArray?) {}
        override fun onEndOfSpeech() {
            //finished()
            println("Speech recognition finished!")
            // changing the color of your mic icon to
            // gray to indicate it is not listening or do something you want
        }

        override fun onError(i: Int) {}

        override fun onResults(bundle: Bundle) {
            val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (result != null) {
                // attaching the output
                // to our viewmodel
                vm.textFromSpeech = result[0]
                println("Result: ${result[0]}")
            }
        }

        override fun onPartialResults(bundle: Bundle) {}
        override fun onEvent(i: Int, bundle: Bundle?) {}

    })
    speechRecognizer.startListening(speechRecognizerIntent)
}