package com.themattburton.cs449.babyrecognitionprogram.dao;

import android.net.Uri;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FlashCard {

    //@PrimaryKey(autoGenerate=true)
    //@NonNull
    //private int cardId;

    @PrimaryKey
    @NonNull
    private String cardTextLabel;

    private String imageUri;
    //private String cardId;

    private String cardAudioFileName;

    public FlashCard(@NonNull String cardTextLabel, String imageUri, String cardAudioFileName) {
        this.cardTextLabel = cardTextLabel;
        this.imageUri = imageUri;
        this.cardAudioFileName = cardAudioFileName;
    }

/*    public FlashCard(int imageNum, String text, String audioFile){
        //this.cardId = new Date().getTime() + "_card";
        this.imageNum = imageNum;
        this.cardTextLabel = text;
        this.cardAudioFileName = audioFile;
    }*/

/*    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }*/

    //public String getCardId() {
    //    return cardId;
    //}

    //public void setCardId(String cardId) {
    //    this.cardId = cardId;
    //}

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCardTextLabel() {
        return cardTextLabel;
    }

    public void setCardTextLabel(String cardTextLabel) {
        this.cardTextLabel = cardTextLabel;
    }

    public String getCardAudioFileName() {
        return cardAudioFileName;
    }

    public void setCardAudioFileName(String cardAudioFileName) {
        this.cardAudioFileName = cardAudioFileName;
    }


}
