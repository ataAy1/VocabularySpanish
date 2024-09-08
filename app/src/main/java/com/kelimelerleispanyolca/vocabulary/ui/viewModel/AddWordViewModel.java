package com.kelimelerleispanyolca.vocabulary.ui.viewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;


import com.kelimelerleispanyolca.vocabulary.data.entity.VocabularyEntity;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.data.repo.WordsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel

public class AddWordViewModel extends ViewModel {

   public WordsRepository krepo;

    @Inject
    public AddWordViewModel(WordsRepository krepo) {
        this.krepo = krepo;
    }

    public void addWord(String spanishWord, String turkishWord, String wordType, String exampleSentence, String exampleSentenceTranslation) {
        WordSaveEntity wordEntity = new WordSaveEntity(0, spanishWord, turkishWord, wordType, exampleSentence, exampleSentenceTranslation, true, true);
        krepo.saveWord(wordEntity);
    }

}