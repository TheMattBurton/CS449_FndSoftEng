package com.themattburton.cs449.babyrecognitionprogram.views;

import android.app.Application;

import com.themattburton.cs449.babyrecognitionprogram.dao.FlashCard;
import com.themattburton.cs449.babyrecognitionprogram.dao.FlashcardRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FlashcardViewModel extends AndroidViewModel {
    private FlashcardRepository flashcardRepository;
    private LiveData<List<FlashCard>> allCards;

    public FlashcardViewModel(@NonNull Application application) {
        super(application);
        flashcardRepository = new FlashcardRepository(application);
        allCards = flashcardRepository.getAllCards();
    }

    public void insert (FlashCard card) {
        flashcardRepository.insert(card);
    }

    public void update (FlashCard card) {
        flashcardRepository.update(card);
    }

    public void delete (FlashCard card) {
        flashcardRepository.delete(card);
    }

    public void deleteAllCards (FlashCard card) {
        flashcardRepository.deleteAllCards();
    }

    public LiveData<List<FlashCard>> getAllCards () {
        return allCards;
    }
}
