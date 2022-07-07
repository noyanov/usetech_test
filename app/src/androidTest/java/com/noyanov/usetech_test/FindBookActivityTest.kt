package com.noyanov.usetech_test

import android.view.View
import android.view.ViewTreeObserver
import androidx.test.core.app.launchActivity
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.any
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FindBookActivityTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule var activityScenarioRule = activityScenarioRule<FindBookActivity>()


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.noyanov.usetech_test", appContext.packageName)
    }

    @Test fun justRun() {
        //val activityScenario = launch(FindBookActivity::class.java)
        launchActivity<FindBookActivity>().use {
        }
    }

    @Test
    fun startSearch() {
        onView(withId(R.id.idEdtSearchBooks)).perform(typeText("Alice"))
        onView(withId(R.id.idBtnSearch)).perform(click())
        onView(isRoot()).perform(waitUntilShown(R.id.idTVBookTitle, 5000));
        onView(withText("Alice's Adventures in Wonderland")).perform(waitUntil(isDisplayed()))
        onView(withText("Alice's Adventures in Wonderland")).check(matches(isDisplayed()))
    }

    @Test
    fun searchAndClick() {
        onView(withId(R.id.idEdtSearchBooks)).perform(typeText("Alice"))
        onView(withId(R.id.idBtnSearch)).perform(click())
        onView(isRoot()).perform(waitUntilShown(R.id.idTVBookTitle, 5000));
        onView(withText("Alice's Adventures in Wonderland")).perform(click())
    }


    fun waitUntil(matcher: Matcher<View>): ViewAction = object : ViewAction {

        override fun getConstraints(): Matcher<View> {
            return any(View::class.java)
        }

        override fun getDescription(): String {
            return StringDescription().let {
                matcher.describeTo(it)
                "wait until: $it"
            }
        }

        override fun perform(uiController: UiController, view: View) {
            if (!matcher.matches(view)) {
                ViewPropertyChangeCallback(matcher, view).run {
                    try {
                        IdlingRegistry.getInstance().register(this)
                        view.viewTreeObserver.addOnDrawListener(this)
                        uiController.loopMainThreadUntilIdle()
                    } finally {
                        view.viewTreeObserver.removeOnDrawListener(this)
                        IdlingRegistry.getInstance().unregister(this)
                    }
                }
            }
        }
    }
    private class ViewPropertyChangeCallback(private val matcher: Matcher<View>, private val view: View) : IdlingResource, ViewTreeObserver.OnDrawListener {

        private lateinit var callback: IdlingResource.ResourceCallback
        private var matched = false

        override fun getName() = "View property change callback"

        override fun isIdleNow() = matched

        override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
            this.callback = callback
        }

        override fun onDraw() {
            matched = matcher.matches(view)
            callback.onTransitionToIdle()
        }
    }


    /**
     * Perform action of waiting until the element is accessible & not shown.
     * @param viewId The id of the view to wait for.
     * @param millis The timeout of until when to wait for.
     */
    fun waitUntilShown(viewId: Int, millis: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with id <$viewId> is shown during $millis millis."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + millis
                val viewMatcher = withId(viewId)
                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child) && child.isShown) {
                            return
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)
                throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
            }
        }
    }

}
