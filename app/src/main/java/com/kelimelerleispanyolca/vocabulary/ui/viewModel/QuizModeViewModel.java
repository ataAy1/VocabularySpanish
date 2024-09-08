package com.kelimelerleispanyolca.vocabulary.ui.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.data.repo.WordsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class QuizModeViewModel extends ViewModel {

    private final WordsRepository wordsRepository;
    private final MutableLiveData<Integer> seenWordsCount = new MutableLiveData<>();
    private final MutableLiveData<List<WordSaveEntity>> quizQuestionsLiveData = new MutableLiveData<>();

    @Inject
    public QuizModeViewModel(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;
        fetchQuizQuestions();
    }

    public LiveData<List<WordSaveEntity>> getRandomKelimeler() {
        return quizQuestionsLiveData;
    }

    public void fetchQuizQuestions() {
        wordsRepository.getQuizQuestions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<WordSaveEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<WordSaveEntity> quizQuestions) {
                        quizQuestionsLiveData.setValue(quizQuestions);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("QuizModeViewModel", "Error fetching quiz questions", e);
                    }
                });
    }

    public void loadSavedWordsCount() {
        wordsRepository.getSavedWordsCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Integer count) {
                        seenWordsCount.setValue(count);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("QuizModeViewModel", "Error fetching saved words count", e);
                    }
                });
    }

    public LiveData<Integer> observeSeenWordsCount() {
        return seenWordsCount;
    }

    public void markWordAsSeen(int id) {
        wordsRepository.markQuizWordAsSeen(id);
    }


    public void resetWordSeenStatus() {
        wordsRepository.resetWordSeen()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, e -> Log.e("QuizModeViewModel", "Error resetting word seen status"));
    }
}
