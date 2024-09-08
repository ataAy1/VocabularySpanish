package com.kelimelerleispanyolca.vocabulary.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.data.repo.WordsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ListeningModeViewModel extends ViewModel {

    private final MutableLiveData<List<WordSaveEntity>> randomKelime = new MutableLiveData<>();
    private final MutableLiveData<Integer> counterListening = new MutableLiveData<>();

    private final WordsRepository krepo;

    @Inject
    public ListeningModeViewModel(WordsRepository krepo) {
        this.krepo = krepo;
        fetchRandomWordsForListening();
        fetchQuizSeenWordsCount();
    }

    public LiveData<List<WordSaveEntity>> getRandomKelime() {
        return randomKelime;
    }

    public MutableLiveData<Integer> getCounterListening() {
        return counterListening;
    }

    public void fetchRandomWordsForListening() {
        krepo.getListeningWords().observeForever(randomKelime::setValue);
    }

    public void fetchQuizSeenWordsCount() {
        krepo.getQuizSeenCountLiveData().observeForever(counterListening::setValue);
    }

    public void markWordAsSeenInWritingMode(int wordId) {
        krepo.markWordAsSeen(wordId);
    }

    public void updateRepeatedWordsData() {
        krepo.getRepeatedWordGraphData();
    }

    public void resetSeenWords() {
        krepo.resetWordSeen();
    }
}
