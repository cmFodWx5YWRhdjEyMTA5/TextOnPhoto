package com.dev.signatureonphoto.util;

import com.dev.signatureonphoto.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class ErrorTestUtil {

    public static void checkErrorViewsDisplay() {
        onView(allOf(withText(R.string.error_title), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withText(R.string.error_message), isDisplayed()))
                .check(matches(isDisplayed()));
    }

    public static void checkClickingReloadShowsContentWithText(String expectedText) {
        onView(allOf(withText(R.string.error_reload), isDisplayed())).perform(click());
        onView(withText(expectedText)).check(matches(isDisplayed()));
    }
}
