package com.kelimelerleispanyolca.vocabulary.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wordGraph")
public class WordGraphDataEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "learnedWords")
    private int learnedWords;

    @ColumnInfo(name = "repeatedWords")
    private int repeatedWords;

    public WordGraphDataEntity(int learnedWords, int repeatedWords, String date) {
        this.learnedWords = learnedWords;
        this.repeatedWords = repeatedWords;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLearnedWords() {
        return learnedWords;
    }

    public void setLearnedWords(int learnedWords) {
        this.learnedWords = learnedWords;
    }

    public int getRepeatedWords() {
        return repeatedWords;
    }

    public void setRepeatedWords(int repeatedWords) {
        this.repeatedWords = repeatedWords;
    }
}
