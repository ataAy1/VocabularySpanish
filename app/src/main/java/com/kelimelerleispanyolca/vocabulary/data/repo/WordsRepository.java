package com.kelimelerleispanyolca.vocabulary.data.repo;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kelimelerleispanyolca.vocabulary.data.entity.VocabularyEntity;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.room.WordsDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class WordsRepository {

    private MutableLiveData<List<VocabularyEntity>> vocabularyList;
    private MutableLiveData<Integer> wordVisibilityCount;
    private MutableLiveData<Integer> wordSeenCount;
    private MutableLiveData<Integer> learnedWordGraphData;
    private MutableLiveData<Integer> repeatedWordGraphData;
    private MutableLiveData<Integer> learningModeWordCount;
    private MutableLiveData<List<WordSaveEntity>> searchedWords;
    private MutableLiveData<List<WordSaveEntity>> quizQuestions;
    private MutableLiveData<List<WordSaveEntity>> writingWords;
    private MutableLiveData<List<WordSaveEntity>> listeningWords;
    private MutableLiveData<List<WordSaveEntity>> allWords;
    private MutableLiveData<Integer> writingWordCount;
    private MutableLiveData<Integer> registeredWordCount;

    private WordsDao wordsDao;

    @Inject
    public WordsRepository(WordsDao wordsDao) {
        this.wordsDao = wordsDao;
        initializeLiveData();
    }

    private void initializeLiveData() {
        vocabularyList = new MutableLiveData<>();
        wordVisibilityCount = new MutableLiveData<>();
        wordSeenCount = new MutableLiveData<>();
        learnedWordGraphData = new MutableLiveData<>();
        repeatedWordGraphData = new MutableLiveData<>();
        learningModeWordCount = new MutableLiveData<>();
        searchedWords = new MutableLiveData<>();
        quizQuestions = new MutableLiveData<>();
        writingWords = new MutableLiveData<>();
        listeningWords = new MutableLiveData<>();
        allWords = new MutableLiveData<>();
        writingWordCount = new MutableLiveData<>();
        registeredWordCount = new MutableLiveData<>();
    }

    public MutableLiveData<List<WordSaveEntity>> getWritingModeWords() {
        MutableLiveData<List<WordSaveEntity>> liveData = new MutableLiveData<>();
        wordsDao.getWritingModeWord()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<WordSaveEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<WordSaveEntity> words) {
                        liveData.setValue(words);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
        return liveData;
    }

    public MutableLiveData<Integer> getWritingWordCount() {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        wordsDao.getRepeatedWordsCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Integer count) {
                        liveData.setValue(count);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
        return liveData;
    }




    public void markWordAsSeen(int wordId) {
        wordsDao.markWordAsSeen(wordId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteVocabularyById(int id) {
        wordsDao.deleteVocabularyById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    public void markWordToShow(int wordId) {
        wordsDao.markWordToShow(wordId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public Completable resetWordSeen() {
        return wordsDao.resetWordSeen();
    }





    public MutableLiveData<List<WordSaveEntity>> searchWord(String query) {
        wordsDao.searchVocabulary(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<WordSaveEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<WordSaveEntity> results) {
                        searchedWords.postValue(results);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
        return searchedWords;
    }

    public void getTotalWordsSeen() {
        wordsDao.countSeenVocabulary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Integer count) {
                        wordSeenCount.setValue(count);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    public MutableLiveData<Integer> getWordSeenCount() {
        return wordSeenCount;
    }

    public void saveWord(WordSaveEntity word) {
        wordsDao.insertVocabulary(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    public MutableLiveData<List<VocabularyEntity>> getRandomVocabulary() {
        wordsDao.getRandomVocabulary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<VocabularyEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<VocabularyEntity> vocabularies) {
                        vocabularyList.setValue(vocabularies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error", "Failed to fetch data", e);
                    }
                });
        return vocabularyList;
    }

    // Quiz
    public Single<List<WordSaveEntity>> getQuizQuestions() {
        return wordsDao.getQuizWords();
    }


    public MutableLiveData<Integer> getQuizSeenCountLiveData() {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();

        wordsDao.countSeenWordsInQuiz()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Integer count) {
                        liveData.setValue(count);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });

        return liveData;
    }



    public Single<Integer> getSavedWordsCount() {
        return wordsDao.getSavedWordsCount();
    }

    public void markQuizWordAsSeen(int wordId) {
        wordsDao.markWordAsSeen(wordId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }



    // Writing



    // Listening


    public MutableLiveData<List<WordSaveEntity>> getListeningWords() {
        return listeningWords;
    }

    // Get all words

    public MutableLiveData<List<WordSaveEntity>> getAllWords() {
        wordsDao.getAllWords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<WordSaveEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<WordSaveEntity> words) {
                        allWords.setValue(words);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
        return allWords;
    }


    public Completable updateWord(WordSaveEntity word) {
        return wordsDao.updateWord(word);
    }

    public Completable deleteWord(int id) {
        return wordsDao.deleteWordById(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Single<String> getDateNow() {

        return Single.fromCallable(() -> {

            return java.time.LocalDate.now().toString();
        });
    }

    public MutableLiveData<Integer> getLearnedWordGraphData() {
        return learnedWordGraphData;
    }

    public MutableLiveData<Integer> getRepeatedWordGraphData() {
        return repeatedWordGraphData;
    }

}
