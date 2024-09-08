package com.kelimelerleispanyolca.vocabulary.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kelimelerleispanyolca.vocabulary.data.repo.WordsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ModeSelectionViewModel extends ViewModel {

    private final WordsRepository repo;
    private final MutableLiveData<Integer> savedWordsCount = new MutableLiveData<>();

    @Inject
    public ModeSelectionViewModel(WordsRepository repo) {
        this.repo = repo;
        loadSavedWordsCount();
    }

    private void loadSavedWordsCount() {
        repo.getSavedWordsCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Integer count) {
                        savedWordsCount.setValue(count);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    public LiveData<Integer> getSavedWordsCountLiveData() {
        return savedWordsCount;
    }
}
