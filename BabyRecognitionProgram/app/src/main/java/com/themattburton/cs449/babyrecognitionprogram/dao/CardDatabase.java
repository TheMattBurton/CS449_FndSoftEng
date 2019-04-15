package com.themattburton.cs449.babyrecognitionprogram.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FlashCard.class}, version = 1)
public abstract class CardDatabase extends RoomDatabase {

    private static CardDatabase INSTANCE;

    public abstract FlashCardDao cardModel();

    public static synchronized CardDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CardDatabase.class, "flashcard_database")
            .fallbackToDestructiveMigration()
            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
