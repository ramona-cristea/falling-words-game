package com.game.fallingwords.views;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.game.fallingwords.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StartGameActivityTest {

    @Rule
    public ActivityTestRule<StartGameActivity> mActivityTestRule = new ActivityTestRule<>(StartGameActivity.class);

    @Test
    public void startGameActivityTest() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_single_player), withText("Single Player"),
                        childAtPosition(
                                allOf(withId(R.id.fullscreen_content),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());
        ViewInteraction textView = onView(
                allOf(withId(R.id.text_counter_correct_answers), withText("Correct answers 0"),
                        childAtPosition(
                                allOf(withId(R.id.fullscreen_content),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Correct answers 0")));
    }

    @Test
    public void restartGameActivityTest() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_single_player), withText("Single Player"),
                        childAtPosition(
                                allOf(withId(R.id.fullscreen_content),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.button_restart),
                        childAtPosition(
                                allOf(withId(R.id.layout_controls),
                                        childAtPosition(
                                                withId(R.id.fullscreen_content),
                                                8)),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.text_counter_correct_answers), withText("Correct answers 0"),
                        childAtPosition(
                                allOf(withId(R.id.fullscreen_content),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Correct answers 0")));
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
