package com.example.quiz5.MainActivityTests;


import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quiz5.MainActivity;
import com.example.quiz5.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);


    /**
     * Tests that the correct activity (QuizActivity) is launched when pressing the quiz button
     */
    @Test
    public void pressQuizButton(){
        Espresso.onView(withId(R.id.Quiz)).perform(click());

        Espresso.onView(withId(R.id.QuizOption1_BUTTON))
                .check(matches(isDisplayed()));
    }


    /**
     * Tests that the correct activity (GalleryActivity) is launched when pressing the GalleryButton
     */
    @Test
    public void pressGalleryButton(){
        Espresso.onView(withId(R.id.MAIN_galleryButton)).perform(click());

        Espresso.onView(withId(R.id.GallerySort_Button))
                .check(matches(isDisplayed()));
    }

}
