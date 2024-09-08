package com.kelimelerleispanyolca.vocabulary.di;

import android.content.Context;

import com.kelimelerleispanyolca.vocabulary.room.VocabularyDatabase;
import com.kelimelerleispanyolca.vocabulary.room.WordsDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public VocabularyDatabase provideDatabase(@ApplicationContext Context context) {
        return VocabularyDatabase.getInstance(context);
    }

    @Provides
    public WordsDao provideKelimelerDao(VocabularyDatabase db) {
        return db.wordsDao();

    }
}
