package com.kelimelerleispanyolca.vocabulary.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kelimelerleispanyolca.vocabulary.data.entity.WordGraphDataEntity;
import com.kelimelerleispanyolca.vocabulary.data.entity.WordSaveEntity;
import com.kelimelerleispanyolca.vocabulary.data.entity.VocabularyEntity;

@Database(entities = {VocabularyEntity.class, WordSaveEntity.class, WordGraphDataEntity.class}, version = 2, exportSchema = false)
public abstract class VocabularyDatabase extends RoomDatabase {

    public abstract WordsDao wordsDao();

    private static volatile VocabularyDatabase INSTANCE;

    public static VocabularyDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (VocabularyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    VocabularyDatabase.class,
                                    "VocabularySpanishRoomDB"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
