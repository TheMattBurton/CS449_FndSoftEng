package com.themattburton.cs449.babyrecognitionprogram.activities;

import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.themattburton.cs449.babyrecognitionprogram.R;
import com.themattburton.cs449.babyrecognitionprogram.dao.FlashCard;
import com.themattburton.cs449.babyrecognitionprogram.views.FlashcardViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SlideShowActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SlideShow ";
    private FlashcardViewModel flashcardViewModel;
    private static MediaPlayer myAudioPlayer = null;

    /**
     * The {@link //android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link //android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static List<FlashCard> cards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        //flashcardViewModel = ViewModelProviders.of(this).get(FlashcardViewModel.class);
        /*flashcardViewModel.getAllCards().observe(this, new Observer<List<FlashCard>>() {
            @Override
            public void onChanged(List<FlashCard> flashCards) {
//TODO
                setCards(flashCards);
            }
        });*/

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                flashcardViewModel = ViewModelProviders.of(SlideShowActivity.this).get(FlashcardViewModel.class);
                setCards(flashcardViewModel.getAllCards());
            }
        });
        //setCards(flashcardViewModel.getAllCards());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void setCards (List<FlashCard> cards) {
        this.cards = cards;
    }

    private static void playRecording(String file) {

        myAudioPlayer = new MediaPlayer();
        try {
            myAudioPlayer.setDataSource(file);
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

    private static void stopPlaying() {
        myAudioPlayer.release();
        myAudioPlayer = null;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_CARD_LABEL_TEXT = "card_label_text";
        private static final String ARG_CARD_IMAGE_URI = "card_image_uri";
        private static final String ARG_CARD_AUDIO_FILE_PATH = "card_audio_file_path";
        private final String[] testCardText = {"APPLE", "BALL", "CAT"};
        private final int[] testCardImage = {R.drawable.apple_640, R.drawable.ball_640, R.drawable.cat_640};

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //args.putString(ARG_CARD_LABEL_TEXT, cards.get(sectionNumber).getCardTextLabel());
            //args.putString(ARG_CARD_IMAGE_URI, cards.get(sectionNumber).getImageUri());
            //args.putString(ARG_CARD_AUDIO_FILE_PATH, cards.get(sectionNumber).getCardAudioFileName());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //View rootView = inflater.inflate(R.layout.fragment_slide_show, container, false);
            View rootView = inflater.inflate(R.layout.card_layout, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView textView = rootView.findViewById(R.id.card_layout_text_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            //textView.setText(testCardText[getArguments().getInt(ARG_SECTION_NUMBER) - 1]); //getArguments().getString(ARG_CARD_LABEL_TEXT));
            textView.setText(cards.get(getArguments().getInt(ARG_SECTION_NUMBER) - 1).getCardTextLabel());
            ImageView imageView = rootView.findViewById(R.id.card_layout_image);
            //imageView.setImageResource(testCardImage[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);
            String uri = cards.get(getArguments().getInt(ARG_SECTION_NUMBER) - 1).getImageUri();
            String decodedUri = uri.replace("%3A", ":");
            imageView.setImageURI(Uri.parse(decodedUri));

            /*imageView.setImageBitmap(BitmapFactory.decodeFile(getArguments().getString(ARG_CARD_IMAGE_URI)));
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v){
                    playRecording(getArguments().getString(ARG_CARD_AUDIO_FILE_PATH));
                }
            });*/
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
            //return cards.size();
        }
    }
}
