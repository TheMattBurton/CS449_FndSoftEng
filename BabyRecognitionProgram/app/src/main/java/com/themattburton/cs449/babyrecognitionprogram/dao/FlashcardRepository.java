package com.themattburton.cs449.babyrecognitionprogram.dao;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class FlashcardRepository {
    private FlashCardDao flashCardDao;
    private List<FlashCard> allCards;

    public FlashcardRepository(Application application) {
        CardDatabase database = CardDatabase.getInstance(application);
        flashCardDao = database.cardModel();
        allCards = flashCardDao.getAllCards();
    }

    public void insert(FlashCard flashCard) {
        new InsertCardAsyncTask(flashCardDao).execute(flashCard);
    }

    public void update(FlashCard flashCard) {
        new UpdateCardAsyncTask(flashCardDao).execute(flashCard);
    }

    public void delete(FlashCard flashCard) {
        new DeleteCardAsyncTask(flashCardDao).execute(flashCard);
    }

    public void deleteAllCards() {
        new DeleteAllCardsAsyncTask(flashCardDao).execute();
    }

    public List<FlashCard> getAllCards() {
        return allCards;
    }

    private static class InsertCardAsyncTask extends AsyncTask<FlashCard, Void, Void> {
        private FlashCardDao flashCardDao;

        private InsertCardAsyncTask(FlashCardDao flashCardDao) {
            this.flashCardDao = flashCardDao;
        }
        @Override
        protected Void doInBackground(FlashCard... flashCards) {
            flashCardDao.insert(flashCards[0]);
            return null;
        }
    }

    private static class UpdateCardAsyncTask extends AsyncTask<FlashCard, Void, Void> {
        private FlashCardDao flashCardDao;

        private UpdateCardAsyncTask(FlashCardDao flashCardDao) {
            this.flashCardDao = flashCardDao;
        }
        @Override
        protected Void doInBackground(FlashCard... flashCards) {
            flashCardDao.updateCards(flashCards[0]);
            return null;
        }
    }

    private static class DeleteCardAsyncTask extends AsyncTask<FlashCard, Void, Void> {
        private FlashCardDao flashCardDao;

        private DeleteCardAsyncTask(FlashCardDao flashCardDao) {
            this.flashCardDao = flashCardDao;
        }
        @Override
        protected Void doInBackground(FlashCard... flashCards) {
            flashCardDao.deleteCards(flashCards[0]);
            return null;
        }
    }

    private static class DeleteAllCardsAsyncTask extends AsyncTask<Void, Void, Void> {
        private FlashCardDao flashCardDao;

        private DeleteAllCardsAsyncTask(FlashCardDao flashCardDao) {
            this.flashCardDao = flashCardDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            flashCardDao.deleteAllCards();
            return null;
        }
    }
}
