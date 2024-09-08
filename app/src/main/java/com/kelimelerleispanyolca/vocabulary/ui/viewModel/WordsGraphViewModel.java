package com.kelimelerleispanyolca.vocabulary.ui.viewModel;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kelimelerleispanyolca.vocabulary.data.repo.WordsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel

public class WordsGraphViewModel extends ViewModel {

    WordsRepository krepo;
    private MutableLiveData<String> dateLiveData;

    public MutableLiveData<Integer> repeatedWordsCount;

    public LiveData<Integer> learnedWordsCount;

    @Inject
    public WordsGraphViewModel(WordsRepository krepo) {
        this.krepo = krepo;
        dateLiveData = new MutableLiveData<>();
        repeatedWordsCount = new MutableLiveData<>();
        learnedWordsCount = new MutableLiveData<>();
        loadLearnedWordsGraph();
        loadRepeatedWordsGraph();
    }

    public void loadLearnedWordsGraph() {
        learnedWordsCount = krepo.getLearnedWordGraphData();
    }


    public void loadRepeatedWordsGraph() {
        repeatedWordsCount = krepo.getRepeatedWordGraphData();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadCurrentDate() {
        krepo.getDateNow()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dateNow -> {
                            dateLiveData.setValue(dateNow);
                        },
                        throwable -> {
                        }
                );
    }

    public LiveData<String> getDateLiveData() {
        return dateLiveData;
    }


}


