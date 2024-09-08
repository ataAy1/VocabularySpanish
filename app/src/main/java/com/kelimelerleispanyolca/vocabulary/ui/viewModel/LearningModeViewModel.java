package com.kelimelerleispanyolca.vocabulary.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kelimelerleispanyolca.vocabulary.data.entity.VocabularyEntity;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.data.repo.WordsRepository;

import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LearningModeViewModel extends ViewModel {

    private final WordsRepository wordsRepository;
    private final MutableLiveData<List<VocabularyEntity>> vocabularyListLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> showAgainWordCountLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> seenWordsCountLiveData = new MutableLiveData<>();

    @Inject
    public LearningModeViewModel(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;
        fetchWordSeenCount();
        fetchTotalSeenWordsCount();
        fetchRandomVocabulary();
    }

    public LiveData<List<VocabularyEntity>> getVocabularyListLiveData() {
        return vocabularyListLiveData;
    }

    public LiveData<Integer> getShowAgainWordCountLiveData() {
        return showAgainWordCountLiveData;
    }

    public LiveData<Integer> getSeenWordsCountLiveData() {
        return seenWordsCountLiveData;
    }

    public void resetSeenWordsCount() {
        wordsRepository.getTotalWordsSeen();
    }

    public void deleteVocabularyById(int ID) {
        wordsRepository.deleteVocabularyById(ID);
    }

    public void fetchTotalSeenWordsCount() {
        wordsRepository.getTotalWordsSeen();
    }

    public void fetchRandomVocabulary() {
        wordsRepository.getRandomVocabulary().observeForever(vocabularyList -> {
            vocabularyListLiveData.setValue(vocabularyList);
        });
    }

    public void fetchWordSeenCount() {
        wordsRepository.getWordSeenCount().observeForever(wordSeenCount -> {
            showAgainWordCountLiveData.setValue(wordSeenCount);
        });
    }

    public void saveWordLearningRecord(WordSaveEntity kelimeKaydetme) {
        wordsRepository.saveWord(kelimeKaydetme);
    }

    public void markWordAsSeen(int kelimeId) {
        wordsRepository.markWordAsSeen(kelimeId);
    }

    public void updateLearnedWordsData() {
        wordsRepository.getLearnedWordGraphData();
    }
}
