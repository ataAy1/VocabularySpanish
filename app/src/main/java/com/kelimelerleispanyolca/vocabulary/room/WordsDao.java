package com.kelimelerleispanyolca.vocabulary.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kelimelerleispanyolca.vocabulary.data.entity.VocabularyEntity;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordGraphDataEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface WordsDao {

    @Query("DELETE FROM vocabulary WHERE id = :id")
    Single<Integer> deleteVocabularyById(int id);

    @Query("SELECT * FROM savedWords WHERE turkishWord LIKE :searchTerm || '%'")
    Single<List<WordSaveEntity>> searchVocabulary(String searchTerm);

    @Query("SELECT COUNT(*) FROM savedWords")
    Single<Integer> getSavedWordsCount();

    @Insert
    Completable insertVocabulary(WordSaveEntity wordSaveEntity);

    @Query("SELECT * FROM vocabulary WHERE show_word != 0 AND word_seen != 0 ORDER BY RANDOM() LIMIT 1")
    Single<List<VocabularyEntity>> getRandomVocabulary();

    @Query("UPDATE vocabulary SET word_seen = 1 WHERE word_seen = 0 AND show_word = 1")
    Completable resetWordSeen();


    @Query("UPDATE vocabulary SET word_seen = 0 WHERE id = :id")
    Completable markWordAsSeen(int id);

    @Query("UPDATE vocabulary SET show_word = 0 WHERE id = :id")
    Completable markWordToShow(int id);

    @Query("SELECT COUNT(*) FROM vocabulary WHERE word_seen = 1 AND show_word = 1")
    Single<Integer> countSeenVocabulary();


    // Quiz mode


    @Query("SELECT * FROM savedWords WHERE wordSeen != 0 ORDER BY RANDOM() LIMIT 4")
    Single<List<WordSaveEntity>> getQuizWords();

    @Query("SELECT COUNT(*) FROM savedWords WHERE wordSeen = 1 AND showWord=1")
    Single<Integer> countSeenWordsInQuiz();

    // Writing mode

    @Query("SELECT * FROM savedWords WHERE wordSeen != 0 ORDER BY RANDOM() LIMIT 1")
    Single<List<WordSaveEntity>> getWritingModeWord();


    // Vocabulary

    @Query("SELECT * FROM savedWords ORDER BY spanishWord ASC")
    Single<List<WordSaveEntity>> getAllWords();

    @Update
    Completable updateWord(WordSaveEntity word);

    @Query("DELETE FROM savedWords WHERE wordId = :id")
    Completable deleteWordById(int id);

    // Graph data

    @Query("SELECT repeatedWords FROM wordGraph")
    Single<Integer> getRepeatedWordsCount();

}
