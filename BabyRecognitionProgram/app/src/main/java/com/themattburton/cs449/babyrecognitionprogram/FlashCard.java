package com.themattburton.cs449.babyrecognitionprogram;

import java.util.Date;

public class FlashCard {

    private int imageNum;
    private String cardId;
    private String cardTextLabel;
    private String cardAudioFileName;

    public FlashCard(int imageNum, String text, String audioFile){
        this.cardId = new Date().getTime() + "_card";
        this.imageNum = imageNum;
        this.cardTextLabel = text;
        this.cardAudioFileName = audioFile;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getImageNum() {
        return imageNum;
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
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
