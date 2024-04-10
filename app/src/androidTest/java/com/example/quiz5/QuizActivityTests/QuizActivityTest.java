package com.example.quiz5.QuizActivityTests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quiz5.Data.PhotoInfo;
import com.example.quiz5.Quiz.QuizActivity;
import com.example.quiz5.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    @Rule
    public ActivityScenarioRule<QuizActivity> activityRule = new ActivityScenarioRule<>(QuizActivity.class);


    /**
     * Tests that the score is updated correctly
     * When answering right and when answering wrong
     */

    @Test
    public void AnswerWrong() throws InterruptedException {
        PhotoInfo correctAnswer = QuizActivity.getCorrect();
        String[] allOptions = QuizActivity.getAllOptions();
        for (int i = 0; i<allOptions.length; i++){
            if (!allOptions[i].equals(correctAnswer.getName())){
                onView(withText(allOptions[i])).perform(click());
                break;
            }
        }
        TimeUnit.SECONDS.sleep(4);
        onView(withId(R.id.ScoreTextView_GAME)).check(matches(withText("0/1")));

    }

    @Test
    public void AnswerCorrectly() throws InterruptedException {
        PhotoInfo correctAnswer = QuizActivity.getCorrect();
        onView(withText(correctAnswer.getName())).perform(click());
        TimeUnit.SECONDS.sleep(4);
        onView(withId(R.id.ScoreTextView_GAME)).check(matches(withText("1/1")));
    }

}

