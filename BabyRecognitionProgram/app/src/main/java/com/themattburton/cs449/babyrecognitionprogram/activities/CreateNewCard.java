package com.themattburton.cs449.babyrecognitionprogram.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.themattburton.cs449.babyrecognitionprogram.dao.FlashCard;
import com.themattburton.cs449.babyrecognitionprogram.R;
import com.themattburton.cs449.babyrecognitionprogram.views.FlashcardViewModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class CreateNewCard extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecording";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    Boolean isRecording = false;
    Boolean isPlaying = false;
    File currentAudioFile = null;
    Button selectImage;
    ImageButton navHome;
    ImageButton saveNewCardButton;
    ImageButton recordButton;
    ImageButton playOrStopButton;
    ImageView imageView;
    String imageUri;
    EditText imageTextLabel;
    TextView playOrStopText;
    TextView recordText;
    MediaRecorder myAudioRecorder = null;
    MediaPlayer myAudioPlayer = null;
    private int REQUEST_CODE = 1;
    private FlashCard newCard  = null;

    private FlashcardViewModel flashcardViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flashcardViewModel = ViewModelProviders.of(this).get(FlashcardViewModel.class);
        flashcardViewModel.getAllCards().observe(this, new Observer<List<FlashCard>>() {
            @Override
            public void onChanged(List<FlashCard> flashCards) {
//TODO
            }
        });
        permissionToRecordAccepted = ContextCompat.checkSelfPermission(CreateNewCard.this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

        isRecording = false;
        isPlaying = false;
        imageUri = null;

        navHome = findViewById(R.id.homeImageButton);
        saveNewCardButton = findViewById(R.id.saveNewCardButton);
        selectImage = findViewById(R.id.selectImageButton);
        imageView = findViewById(R.id.selectImageView);
        imageTextLabel = findViewById(R.id.newCardTextLabel);
        recordButton = findViewById(R.id.recordAudioButton);
        playOrStopButton = findViewById(R.id.playOrStopAudioButton);
        playOrStopButton.setEnabled(false);
        playOrStopText = findViewById(R.id.playOrStopRecordingLabel);
        recordText = findViewById(R.id.recordVoiceLabel);

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

        recordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!permissionToRecordAccepted) {
                    ActivityCompat.requestPermissions(CreateNewCard.this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                    if (permissionToRecordAccepted) recordButton.callOnClick();
                }else {
                    if (!isRecording) {
                        MediaRecorderReady();
                        startRecording();
                    } else {
                        stopRecording();
                    }
                }
            }
        });

        playOrStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isRecording) {
                    if (null != myAudioPlayer && myAudioPlayer.isPlaying()) {
                        stopPlaying();
                    }else if (currentAudioFile != null) {
                        playRecording();
                    }
                } else {
                    stopRecording();
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (myAudioRecorder != null) {
            myAudioRecorder.release();
            myAudioRecorder = null;
        }

        if (myAudioPlayer != null) {
            myAudioPlayer.release();
            myAudioPlayer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    private void saveNewCard() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image!", Toast.LENGTH_LONG).show();
        } else if (imageTextLabel.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please add a text label to your image!", Toast.LENGTH_LONG).show();
        } else {
            flashcardViewModel.insert(new FlashCard(imageTextLabel.getText().toString(),
                    imageUri, null == currentAudioFile ? null : currentAudioFile.getAbsolutePath()));
            Toast.makeText(this, "New card saved!", Toast.LENGTH_LONG).show();
            imageUri = null;
        }

    }

    private void MediaRecorderReady(){
        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setMaxDuration(5000);
        if (currentAudioFile != null) currentAudioFile.delete();
        currentAudioFile = new File(getApplicationContext().getCacheDir(), getNewTempAudioFilename());
        myAudioRecorder.setOutputFile(currentAudioFile.getAbsolutePath());
        myAudioRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopRecording();
                }
            }
        });
    }

    private void startRecording() {
        isRecording = true;
        try {
            myAudioRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Audio Recorder prepare() failed");
        }
        myAudioRecorder.start();
        playOrStopText.setText("Stop Recording");
        playOrStopText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRecording));
        recordText.setText("Recording . . .");
        recordText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRecording));
        playOrStopButton.setImageResource(R.drawable.ic_stop_red_24dp);
        //changeDrawableColor(R.drawable.ic_stop_black_24dp, R.color.colorRecording);
        recordButton.setImageResource(R.drawable.ic_record_voice_over_red_24dp);
        //changeDrawableColor(R.drawable.ic_record_voice_over_black_24dp, R.color.colorRecording);
    }

    private void stopRecording() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        isRecording = false;
        playOrStopText.setText("Play Recording");
        playOrStopText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPlay));
        recordText.setText("Record voice");
        recordText.setTextColor(Color.BLACK);
        playOrStopButton.setEnabled(true);
        playOrStopButton.setImageResource(R.drawable.ic_play_circle_outline_green_24dp);
        recordButton.setImageResource(R.drawable.ic_record_voice_over_black_24dp);
        //changeDrawableColor(R.drawable.ic_record_voice_over_black_24dp, Color.BLACK);
    }

    private void playRecording() {
        isPlaying = true;
        playOrStopButton.setImageResource(R.drawable.ic_stop_green_24dp);
        //changeDrawableColor(R.drawable.ic_stop_black_24dp, R.color.colorPlay);
        playOrStopText.setText("Stop Playback");
        myAudioPlayer = new MediaPlayer();
        try {
            myAudioPlayer.setDataSource(currentAudioFile.getAbsolutePath());
            myAudioPlayer.prepare();
            myAudioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
            myAudioPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Audio Player prepare() failed");
        }

    }

    private void stopPlaying() {
        myAudioPlayer.release();
        myAudioPlayer = null;
        isPlaying = false;
        playOrStopButton.setImageResource(R.drawable.ic_play_circle_outline_green_24dp);
        playOrStopText.setText("Play Recording");
    }

    private String getNewTempAudioFilename() {
        return System.currentTimeMillis() + "_FlashCardRecording.3gp";
    }

    private void changeDrawableColor(int resId, int color ) {
        Drawable mDrawable = getApplicationContext().getResources().getDrawable(resId);
        mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            imageUri = uri.toString();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
