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
public class WritingModeViewModel extends ViewModel {

    private final MutableLiveData<List<WordSaveEntity>> _getData = new MutableLiveData<>();
    public final LiveData<List<WordSaveEntity>> getData = _getData;

    private final MutableLiveData<Integer> _getCounter = new MutableLiveData<>();
    public final LiveData<Integer> getCounter = _getCounter;

    private final MutableLiveData<Integer> _repeatedWords = new MutableLiveData<Integer>();
    public final LiveData<Integer> repeatedWords = _repeatedWords;

    private final WordsRepository krepo;

    @Inject
    public WritingModeViewModel(WordsRepository krepo) {
        this.krepo = krepo;
        resetSeenWords();
        loadWritingModeWords();
        loadWritingModeSeenWordCount();
    }

    public void loadWritingModeWords() {
        krepo.getWritingModeWords().observeForever(words -> _getData.setValue(words));
    }

    public void loadRepeatedWords() {
        krepo.getRepeatedWordGraphData().observeForever
                (data -> _repeatedWords.setValue(data)
        );
    }

    public void loadWritingModeSeenWordCount() {
        krepo.getWritingWordCount().observeForever(count -> _getCounter.setValue(count));
    }

    public void markWordAsShown(int idAl) {
        krepo.markWordAsSeen(idAl);
    }

    public void resetSeenWords() {
        krepo.resetWordSeen();
    }
}
