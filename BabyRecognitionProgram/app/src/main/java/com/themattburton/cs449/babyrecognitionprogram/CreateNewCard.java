package com.themattburton.cs449.babyrecognitionprogram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

public class CreateNewCard extends AppCompatActivity {

    Button selectImage;
    ImageButton navHome;
    ImageButton saveNewCardButton;
    ImageButton recordButton;
    ImageButton playButton;
    ImageView imageView;
    EditText imageTextLabel;
    MediaRecorder myAudioRecorder = new MediaRecorder();
    private int REQUEST_CODE = 1;
    private FlashCard newCard;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectImage = findViewById(R.id.selectImageButton);
        imageView = findViewById(R.id.selectImageView);
        navHome = findViewById(R.id.homeImageButton);
        imageTextLabel = findViewById(R.id.newCardTextLabel);
        saveNewCardButton = findViewById(R.id.saveNewCardButton);
        recordButton = findViewById(R.id.recordAudioButton);
        playButton = findViewById(R.id.playRecordedAudioButton);

        navHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent1 = new Intent(CreateNewCard.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        saveNewCardButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                saveNewCard();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });


    }

    private void saveNewCard() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
