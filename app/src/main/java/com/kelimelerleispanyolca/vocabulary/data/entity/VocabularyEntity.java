package com.kelimelerleispanyolca.vocabulary.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "vocabulary")
public class VocabularyEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "spanish_word")
    private String spanishWord;

    @ColumnInfo(name = "turkish_translation")
    private String turkishTranslation;

    @ColumnInfo(name = "word_type")
    private String wordType;

    @ColumnInfo(name = "example_sentence_turkish")
    private String exampleSentenceTurkish;

    @ColumnInfo(name = "example_sentence_translation")
    private String exampleSentenceTranslation;

    @ColumnInfo(name = "show_word")
    private boolean showWord;

    @ColumnInfo(name = "word_seen")
    private boolean wordSeen;


    public VocabularyEntity(String spanishWord, String turkishTranslation, String wordType,
                            String exampleSentenceTurkish, String exampleSentenceTranslation,
                            boolean showWord, boolean wordSeen) {
        this.spanishWord = spanishWord;
        this.turkishTranslation = turkishTranslation;
        this.wordType = wordType;
        this.exampleSentenceTurkish = exampleSentenceTurkish;
        this.exampleSentenceTranslation = exampleSentenceTranslation;
        this.showWord = showWord;
        this.wordSeen = wordSeen;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpanishWord() {
        return spanishWord;
    }

    public void setSpanishWord(String spanishWord) {
        this.spanishWord = spanishWord;
    }

    public String getTurkishTranslation() {
        return turkishTranslation;
    }

    public void setTurkishTranslation(String turkishTranslation) {
        this.turkishTranslation = turkishTranslation;
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
