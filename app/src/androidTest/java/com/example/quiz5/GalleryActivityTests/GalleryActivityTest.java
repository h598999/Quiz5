package com.example.quiz5.GalleryActivityTests;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quiz5.Gallery.GalleryActivity;
import com.example.quiz5.R;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class GalleryActivityTest {
    @Rule
    public IntentsTestRule<GalleryActivity> activityRule = new IntentsTestRule<>(GalleryActivity.class);

    @Test
    public void AddImage() throws InterruptedException {
        //Press button
        int photosBefore = GalleryActivity.getAllPhotos().size();
        onView(withId(R.id.new_Button)).perform(click());
        mockPhoto();
        onView(withId(R.id.inputText_input)).perform(typeText("A"));
        closeSoftKeyboard();
        onView(withId(R.id.submit_Button)).perform(click());
        onView(withId(R.id.goButton)).perform(click());
        assertTrue(GalleryActivity.getAllPhotos().size() == photosBefore+1);
    }

    @Test
    public void deletePhoto() throws InterruptedException {
        RecyclerView view = activityRule.getActivity().findViewById(R.id.recyclerImageView_Gallery);
        int before =  view.getAdapter().getItemCount();
        onView(withId(R.id.recyclerImageView_Gallery)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChild(R.id.GalleryDelete_Button)));
        TimeUnit.SECONDS.sleep(2);
        int after = view.getAdapter().getItemCount();
        assertTrue(after == before-1);

    }

    void mockPhoto(){
        //Uri
        Uri picureUri = Uri.parse("android.resource://com.example.quiz5/" + R.drawable.stubbed_image);
        //Intent
        Intent intent = new Intent();
        //setIntentData
        intent.setData(picureUri);

        //intending has action action.getcontent respondwith
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, intent));

        //onView(withAddImage)
        onView(withId(R.id.uploadPhoto_Button)).perform(click());

        //intendend(hasAction
        intended(hasAction(Intent.ACTION_GET_CONTENT));
    }

    public static ViewAction clickChild(final int id){
        return new ViewAction() {
            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}
