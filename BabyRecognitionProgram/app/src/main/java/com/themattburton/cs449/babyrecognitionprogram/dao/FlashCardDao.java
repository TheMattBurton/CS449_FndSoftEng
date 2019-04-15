package com.themattburton.cs449.babyrecognitionprogram.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FlashCardDao {

    // The conflict strategy defines what happens,
    // if there is an existing entry.
    // The default action is ABORT.
    @Insert(onConflict = REPLACE)
    void insert(FlashCard card);

    // Update multiple entries with one call.
    @Update(onConflict = REPLACE)
    void updateCards(FlashCard... cards);

    @Delete
    void deleteCards(FlashCard... cards);

    // Simple query that does not take parameters and returns nothing.
    @Query("DELETE FROM FlashCard")
    void deleteAllCards();

    // Simple query without parameters that returns values.
    @Query("SELECT * from FlashCard ORDER BY cardTextLabel ASC")
    LiveData<List<FlashCard>> getAllCards();

    // Query with parameter that returns a specific word or words.
    @Query("SELECT * FROM FlashCard WHERE cardTextLabel LIKE :word ")
    LiveData<List<FlashCard>> findCard(String word);

/*    @Query("select * from FlashCard where cardId = :id")
    FlashCard loadCardById(int id);*/
}
