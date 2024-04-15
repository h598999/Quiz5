package com.example.quiz5.Quiz;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz5.Data.PhotoInfo;
import com.example.quiz5.Data.PhotoInfoViewModel;
import com.example.quiz5.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String STATE_PLAYS = "Plays";
    private static final String STATE_SCORE = "Score";
    private static final String STATE_ImageData = "Data";
    private static final String STATE_Option1 = "option1";
    private static final String STATE_Option2 = "option2";
    private static final String STATE_Option3 = "option3";
    private static final String State_name = "name";

    public int Score;
    public int Plays;
    private static PhotoInfo currentCorrect;
    private ImageView view;
    private static Button option1;
    private static Button option2;
    private static Button option3;
    private TextView score;
    private TextView countDown;
    private final static int DELAY_MILLIS = 2000;
    private boolean hardmode = false;
    public PhotoInfoViewModel mViewModel;
    private List<PhotoInfo> photoList = new ArrayList<PhotoInfo>();
    private List<Integer> indexList;


    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_quiz_land);
        } else {
            setContentView(R.layout.activity_quiz);
        }

        mViewModel = new PhotoInfoViewModel(getApplication());
        Score = 0;
        indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(1);
        indexList.add(2);
        Score = 0;
        Plays = 0;

        view = findViewById(R.id.QuizImage_IMAGEVIEW);
        option1 = findViewById(R.id.QuizOption1_BUTTON);
        option2 = findViewById(R.id.QuizOption2_BUTTON);
        option3 = findViewById(R.id.QuizOption3_BUTTON);
        score = findViewById(R.id.ScoreTextView_GAME);

        if (savedInstanceBundle != null){
            Plays = savedInstanceBundle.getInt(STATE_PLAYS, 0);
            Score = savedInstanceBundle.getInt(STATE_SCORE,0);
            byte[] imageData = savedInstanceBundle.getByteArray(STATE_ImageData);
            String name = savedInstanceBundle.getString(State_name);
            currentCorrect = new PhotoInfo(name, imageData);
            option1.setText(savedInstanceBundle.getString(STATE_Option1));
            option2.setText(savedInstanceBundle.getString(STATE_Option2));
            option3.setText(savedInstanceBundle.getString(STATE_Option3));
            score.setText(Score + "/" + Plays);
            view.setImageBitmap(BitmapFactory.decodeByteArray(currentCorrect.getImageData(), 0, currentCorrect.getImageData().length));
            mViewModel.getAllPictures().observe(this, pictures -> {
                pictures.forEach(p -> Log.d("Database", "Name: " + p.getName() + "Data: " + p.getImageData()));
                if (!pictures.isEmpty()) {
                    photoList = pictures;
                }
            });
        } else {

            mViewModel.getAllPictures().observe(this, pictures -> {
                pictures.forEach(p -> Log.d("Database", "Name: " + p.getName() + "Data: " + p.getImageData()));
                if (!pictures.isEmpty()) {
                    photoList = pictures;
                    refresh(photoList, indexList);
                }
            });
        }

        option1.setOnClickListener(v -> {
            if (hardmode){
                timer.cancel();
            }
            Boolean isCorrect = checkAnswer(option1.getText().toString());
            answer(isCorrect, option1, option2, option3);
        });
        option2.setOnClickListener(v -> {
            if (hardmode){
                timer.cancel();
            }
            Boolean isCorrect = checkAnswer(option2.getText().toString());
            answer(isCorrect, option2, option1, option3);
        });
        option3.setOnClickListener(v -> {
            if (hardmode){
                timer.cancel();
            }
            Boolean isCorrect = checkAnswer(option3.getText().toString());
            answer(isCorrect, option3, option2, option1);
        });
    }


    private boolean checkAnswer(String name){
        return currentCorrect.getName().equals(name);
    }

    private void answer(Boolean isCorrect, Button pressed, Button notPressed, Button notPressed2){
        if (isCorrect){
            pressed.setClickable(false);
            notPressed.setClickable(false);
            notPressed2.setClickable(false);
            pressed.setBackgroundColor(Color.GREEN);
            Handler handler = new Handler();
            Score++;
            Plays++;
            handler.postDelayed(() -> refresh(photoList, indexList), DELAY_MILLIS);
        } else {
            pressed.setClickable(false);
            notPressed.setClickable(false);
            notPressed2.setClickable(false);
            colourCorrect(pressed, notPressed, notPressed2);
            pressed.setBackgroundColor(Color.RED);
            Handler handler = new Handler();
            Plays++;
            handler.postDelayed(() -> refresh(photoList, indexList), DELAY_MILLIS);
        }
    }

    private void refresh(List<PhotoInfo> photoList, List<Integer> indexList){
        Collections.shuffle(photoList);
        Collections.shuffle(indexList);

        currentCorrect = photoList.get(0);
        view.setImageBitmap(BitmapFactory.decodeByteArray(currentCorrect.getImageData(), 0, currentCorrect.getImageData().length));
        option1.setClickable(true);
        option2.setClickable(true);
        option3.setClickable(true);

        option1.setBackgroundColor(Color.parseColor("#FF673AB7"));
        option2.setBackgroundColor(Color.parseColor("#FF673AB7"));
        option3.setBackgroundColor(Color.parseColor("#FF673AB7"));
        option1.setText(photoList.get(indexList.get(0)).getName());
        option2.setText(photoList.get(indexList.get(1)).getName());
        option3.setText(photoList.get(indexList.get(2)).getName());
        score.setText(Score + "/" + Plays);
        if (hardmode){
            timer = new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    countDown.setText(""+millisUntilFinished / 1000);
                    // logic to set the EditText could go here
                }

                public void onFinish() {
                    Plays++;
                    refresh(photoList, indexList);
                }
            }.start();
        }
    }

    private void colourCorrect(Button option1, Button option2, Button option3){
        if (checkAnswer(option1.getText().toString())){
            option1.setBackgroundColor(Color.GREEN);
        } else if(checkAnswer(option2.getText().toString())){
            option2.setBackgroundColor(Color.GREEN);
        } else {
            option3.setBackgroundColor(Color.GREEN);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Check the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_quiz_land);
            // Update your landscape layout
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_quiz);
            // Update your portrait layout
        }
    }

    public static PhotoInfo getCorrect(){
        return currentCorrect;
    }

    public static String[] getAllOptions(){
        return new String[]{option1.getText().toString(), option2.getText().toString(), option3.getText().toString()};
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        outState.putInt(STATE_SCORE, Score);
        outState.putInt(STATE_PLAYS, Plays);
        outState.putByteArray(STATE_ImageData, currentCorrect.getImageData());
        outState.putString(STATE_Option1, option1.getText().toString());
        outState.putString(STATE_Option2, option2.getText().toString());
        outState.putString(STATE_Option3, option3.getText().toString());
        outState.putString(State_name, currentCorrect.getName());
    }
}


