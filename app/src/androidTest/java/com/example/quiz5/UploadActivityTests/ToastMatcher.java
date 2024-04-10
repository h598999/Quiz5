package com.example.quiz5.UploadActivityTests;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_TOAST;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.annotation.IntRange;
import androidx.test.espresso.Root;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ToastMatcher extends TypeSafeMatcher<Root> {


    private static final int MAX_RETRIES = 3;
    private int maximumRetries;
    private int currentFailures = 0;

    public ToastMatcher(@IntRange(from = 1) int maximumRetries) {
        this.maximumRetries = maximumRetries;
    }

    public ToastMatcher() {
        this(MAX_RETRIES);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("no toast found after ");
    }

    @Override
    public boolean matchesSafely(Root item) {
        Integer type = item.getWindowLayoutParams().get().type;

        if (type == TYPE_TOAST || type == TYPE_APPLICATION_OVERLAY) {
            Object windowToken = item.getDecorView().getWindowToken();
            Object appToken = item.getDecorView().getApplicationWindowToken();

            if (windowToken == appToken) {
                return true;
            }
        }

        return ++currentFailures >= maximumRetries;
    }

    public static ViewInteraction onToast(int text, @IntRange(from = 1) int maximumRetries) {
        return onView(withText(text)).inRoot(new ToastMatcher(maximumRetries));
    }

    public static ViewInteraction onToast(int text) {
        return onToast(text, MAX_RETRIES);
    }

}
