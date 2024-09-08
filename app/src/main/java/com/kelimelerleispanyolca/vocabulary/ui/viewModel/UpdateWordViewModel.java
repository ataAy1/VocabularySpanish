package com.kelimelerleispanyolca.vocabulary.ui.viewModel;

import androidx.lifecycle.ViewModel;

import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.data.repo.WordsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UpdateWordViewModel extends ViewModel {

    private final WordsRepository wordsRepository;

    @Inject
    public UpdateWordViewModel(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;
    }

    public void updateWord(WordSaveEntity updatedWord) {
        wordsRepository.updateWord(updatedWord);
    }

    public void deleteWordById(int id) {
        wordsRepository.deleteWord(id);
    }
}
