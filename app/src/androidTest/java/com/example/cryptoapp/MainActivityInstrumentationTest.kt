package com.example.cryptoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class MainActivityInstrumentationTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun clickRetryButton() {

        // Assuming your retry button has text "Retry"
        onView(withId(R.id.swipeRefreshLayout)).perform(click())
        // Assuming your retry button has text "Retry"
//        onView(withText("Oops!,Something went wrong.\\n Please Retry")).perform(click())


        // Add assertions or verifications based on the behavior you expect
    }
}
