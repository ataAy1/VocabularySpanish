package com.kelimelerleispanyolca.vocabulary.ui.viewModel;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.data.repo.WordsRepository;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WordsViewModel extends ViewModel {

    private final WordsRepository repository;
    private final MutableLiveData<List<WordSaveEntity>> fetchWords = new MutableLiveData<>();
    private TextToSpeech textToSpeech;

    @Inject
    public WordsViewModel(WordsRepository repository) {
        this.repository = repository;
        loadAllWords();
    }

    public LiveData<List<WordSaveEntity>> fetchAllWords() {
        return fetchWords;
    }

    public LiveData<List<WordSaveEntity>> searchWord(String searchTerm) {
        return repository.searchWord(searchTerm);
    }

    public void loadAllWords() {
        repository.getAllWords().observeForever(fetchWords::setValue);
    }

    public void playWordAudio(String word, Context context) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(new Locale("es", "ES"));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    } else {
                        textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }
            });
        } else {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}
