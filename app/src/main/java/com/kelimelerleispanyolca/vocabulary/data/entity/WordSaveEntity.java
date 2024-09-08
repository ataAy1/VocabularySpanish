package com.kelimelerleispanyolca.vocabulary.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "savedWords")
public class WordSaveEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wordId")
    private int wordId;

    @ColumnInfo(name = "spanishWord")
    private String spanishWord;

    @ColumnInfo(name = "turkishWord")
    private String turkishWord;

    @ColumnInfo(name = "wordType")
    private String wordType;

    @ColumnInfo(name = "exampleSentenceTurkish")
    private String exampleSentenceTurkish;

    @ColumnInfo(name = "exampleSentenceTranslation")
    private String exampleSentenceTranslation;

    @ColumnInfo(name = "showWord")
    private boolean showWord;

    @ColumnInfo(name = "wordSeen")
    private boolean wordSeen;

    public WordSaveEntity(int wordId, String spanishWord, String turkishWord, String wordType, String exampleSentenceTurkish, String exampleSentenceTranslation, boolean showWord, boolean wordSeen) {
        this.wordId = wordId;
        this.spanishWord = spanishWord;
        this.turkishWord = turkishWord;
        this.wordType = wordType;
        this.exampleSentenceTurkish = exampleSentenceTurkish;
        this.exampleSentenceTranslation = exampleSentenceTranslation;
        this.showWord = showWord;
        this.wordSeen = wordSeen;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getSpanishWord() {
        return spanishWord;
    }

    public void setSpanishWord(String spanishWord) {
        this.spanishWord = spanishWord;
    }

    public String getTurkishWord() {
        return turkishWord;
    }

    public void setTurkishWord(String turkishWord) {
        this.turkishWord = turkishWord;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getExampleSentenceTurkish() {
        return exampleSentenceTurkish;
    }

    public void setExampleSentenceTurkish(String exampleSentenceTurkish) {
        this.exampleSentenceTurkish = exampleSentenceTurkish;
    }

    public String getExampleSentenceTranslation() {
        return exampleSentenceTranslation;
    }

    public void setExampleSentenceTranslation(String exampleSentenceTranslation) {
        this.exampleSentenceTranslation = exampleSentenceTranslation;
    }

    public boolean isShowWord() {
        return showWord;
    }

    public void setShowWord(boolean showWord) {
        this.showWord = showWord;
    }

    public boolean isWordSeen() {
        return wordSeen;
    }

    public void setWordSeen(boolean wordSeen) {
        this.wordSeen = wordSeen;
    }
}
