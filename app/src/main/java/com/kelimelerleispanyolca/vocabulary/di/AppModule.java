package com.kelimelerleispanyolca.vocabulary.di;

import com.kelimelerleispanyolca.vocabulary.data.repo.WordsRepository;
import com.kelimelerleispanyolca.vocabulary.room.WordsDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.Provides;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {


    @Provides
    @Singleton
    public WordsRepository provideKelimeRepo(WordsDao kelimelerDao) {
        return new WordsRepository(kelimelerDao);
    }
}
