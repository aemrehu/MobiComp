package com.codemave.mobicomphw1.ui.home.speechtotext

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SpeechToTextViewModel: ViewModel() {
    var textFromSpeech: String? by mutableStateOf(null)
}