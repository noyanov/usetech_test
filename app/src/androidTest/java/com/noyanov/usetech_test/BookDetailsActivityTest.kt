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
class BookDetailsActivityTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule var activityScenarioRule = activityScenarioRule<BookDetailsActivity>()


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.noyanov.usetech_test", appContext.packageName)
    }

    @Test fun justRun() {
        //val activityScenario = launch(MainActivity::class.java)
        launchActivity<BookDetailsActivity>().use {
        }
    }

    @Test
    fun clickToPreview() {
        onView(withId(R.id.idBtnPreview)).perform(click())
    }

    @Test
    fun clickToBuy() {
        //onView(withId(R.id.idBtnBuy)).perform(click())
    }

    @Test
    fun clickToAddToFavorites() {
        //onView(withId(R.id.idBtnAddToFavorites)).perform(click())
    }

}
