package com.noyanov.usetech_test

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.noyanov.usetech_test", appContext.packageName)
    }

    @Test fun justRun() {
        //val activityScenario = launch(MainActivity::class.java)
        launchActivity<MainActivity>().use {
        }
    }

    @Test
    fun firstItemInBookList() {
        onView(withId(R.id.recyclerview))
        onView(withText("Maximum PC")).check(matches(isDisplayed()));
    }


    @Test
    fun filteringAlice() {
        // Type Search control to open editor
        onView(withId(R.id.idEdtFilterBooks))
            .perform(click())
        // Type Alice there to filter the book
        onView(withId(R.id.idEdtFilterBooks))
            .perform(typeText("Alice"), closeSoftKeyboard())

        // Check that a book is available
        // Click to first element
        onView(withId(R.id.recyclerview)).perform(click())


        // Check that the text was changed.
        //onView(withId(R.id.recyclerview)).check(matches(atPosition(0, withText("Alice's Adventures in Wonderland"))));
        onView(withText("Alice's Adventures in Wonderland")).check(matches(isDisplayed()));
    }

    @Test
    fun clickOnFirstBook() {
        // Check that the text was changed.
        //onView(withId(R.id.recyclerview)).check(matches(atPosition(0, withText("Alice's Adventures in Wonderland"))));
        onView(withText("Maximum PC")).check(matches(isDisplayed()));
        onView(withText("Maximum PC")).perform(click())
    }

    @Test
    fun clickOnFirstBookAfterFiltering() {
        // Type Search control to open editor
        onView(withId(R.id.idEdtFilterBooks))
            .perform(click())
        // Type Alice there to filter the book
        onView(withId(R.id.idEdtFilterBooks))
            .perform(typeText("Alice"), closeSoftKeyboard())

        // Check that a book is available
        // Click to first element
        onView(withId(R.id.recyclerview)).perform(click())


        // Check that the text was changed.
        //onView(withId(R.id.recyclerview)).check(matches(atPosition(0, withText("Alice's Adventures in Wonderland"))));
        onView(withText("Alice's Adventures in Wonderland")).check(matches(isDisplayed()));
        onView(withText("Alice's Adventures in Wonderland")).perform(click())
    }

    fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?>? {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }



    @Test
    fun startAddBook() {
        onView(withId(R.id.fab)).perform(click())
    }


}
