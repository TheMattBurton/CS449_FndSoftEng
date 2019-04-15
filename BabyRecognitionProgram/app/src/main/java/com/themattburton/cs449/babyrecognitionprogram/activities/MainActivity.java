package com.themattburton.cs449.babyrecognitionprogram.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.themattburton.cs449.babyrecognitionprogram.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Button buttonCreateCard = findViewById(R.id.buttonCreateCard);
        Button buttonEditCards = findViewById(R.id.buttonEditCards);
        Button buttonSlideshow = findViewById(R.id.buttonSlideshow);
        buttonCreateCard.setOnClickListener(this);
        buttonEditCards.setOnClickListener(this);
        buttonSlideshow.setOnClickListener(this);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

//    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonCreateCard:
                Intent intent1 = new Intent(this, CreateNewCard.class);
                startActivity(intent1);
                break;
            case R.id.buttonEditCards:
                Intent intent2 = new Intent(this, SlideReviewActivity.class);
                startActivity(intent2);
                break;
            case R.id.buttonSlideshow:
                Intent intent3 = new Intent(this, SlideShowActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }
}
