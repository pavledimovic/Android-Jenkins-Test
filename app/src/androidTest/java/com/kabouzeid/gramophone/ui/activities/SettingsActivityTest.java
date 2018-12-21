package com.kabouzeid.gramophone.ui.activities;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.kabouzeid.gramophone.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<SettingsActivity> mActivityTestRule = new ActivityTestRule<>(SettingsActivity.class);

    @Test
    /** Check Default Settings Configuration
     * Checking that list is present
     * Click on first item in a list
     * Check that child of parent view with title "Library" is opened
     * Check that item "Song" is present and enabled
     * Testing default state of the view
     * That button is clickable, and that react on click action
     * That checkbox can be checked and un-checked
     * Testing view after we applied change, checkbox state should be un-checked
     * exclude list "SONGS" from toolbar in main activity class(State will be verified in next test class: " MainActivityTestWithOngoingPlayback")
     * Checking that lists contain all names(titles).
     * */
    public  void listClickTest() {

        // Testing that list exist, that text exists in the list, that required activity(view) is launched and that buttons are clickable.

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list),
                        childAtPosition(
                                withId(android.R.id.list_container),
                                0)));
        recyclerView.check(ViewAssertions.matches(isDisplayed()));

        onView(withText("Library categories")).perform(click())
                .check(ViewAssertions.matches(withText("Library categories")));
        onView(withText("Songs")).check(ViewAssertions.matches(isDisplayed()))
                .check(ViewAssertions.matches(isEnabled()));

        // Testing the default state of the view, that button is clickable, that react on click action and
        // that checkbox is checked and un-checked after we click on it twice.

        ViewInteraction mDButton = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("OK"),
                        childAtPosition(
                                allOf(withId(R.id.md_root),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        mDButton.perform(click());

        // Testing of selected view after we applied changes, unchecked checkbox that result with exclusion of the playlist "SONGS" from toolbar in main activity.
        // Assertion will be verified in next test class: " MainActivityTestWithOngoingPlayback".

        onView(withText("Library categories")).perform(click());
        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.recycler_view),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)));
        recyclerView3.perform(actionOnItemAtPosition(0, click()));
        ViewInteraction mDButton1 = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("OK"),
                        childAtPosition(
                                allOf(withId(R.id.md_root),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        mDButton.perform(click());
        onView(withText("Library categories")).perform(click());
        ViewInteraction checkBox2 = onView(
                allOf(withId(R.id.checkbox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recycler_view),
                                        0),
                                0),
                        isDisplayed()));
        checkBox2.check(matches(isNotChecked()));

        // Verification that lists contain elements with following titles and that they are enabled.

        onView(withText("Albums")).check(ViewAssertions.matches(isDisplayed()))
                .check(ViewAssertions.matches(isEnabled()));
        onView(withText("Artists")).check(ViewAssertions.matches(isDisplayed()))
                .check(ViewAssertions.matches(isEnabled()));
        onView(withText("Genres")).check(ViewAssertions.matches(isDisplayed()))
                .check(ViewAssertions.matches(isEnabled()));
        onView(withText("Playlists")).check(ViewAssertions.matches(isDisplayed()))
                .check(ViewAssertions.matches(isEnabled()));


    }
    //Declaration for Drag and drop testing.
    public static void drag(Instrumentation inst, float fromX, float toX, float fromY,
                            float toY, int stepCount) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        float y = fromY;
        float x = fromX;

        float yStep = (toY - fromY) / stepCount;
        float xStep = (toX - fromX) / stepCount;

        MotionEvent event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        inst.sendPointerSync(event);
        for (int i = 0; i < stepCount; ++i) {
            y += yStep;
            x += xStep;
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
            inst.sendPointerSync(event);
        }

        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
    }
    public static ViewAction swipeUp() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.BOTTOM_CENTER,
                GeneralLocation.TOP_CENTER, Press.FINGER);
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


