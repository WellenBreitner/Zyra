package com.example.zyra;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import com.example.zyra.studentFragmentAndActivity.create_account_activity;

@RunWith(AndroidJUnit4.class)
public class createAccountUITest {

    @Rule
    public IntentsTestRule<create_account_activity> createAccountTest =
            new IntentsTestRule<>(create_account_activity.class);

    //note the test should be done one by one

    // wellen breitner E2300128 testing "testEmptyFieldErrorMessage()"
    // test if the create account all the text field is empty and user click the create account button
    // the create account page shouldn't move to another page.
    @Test
    public void testEmptyField() {
        // user enter empty value to name, email, and password
        Espresso.onView(withId(R.id.userCreateNameEditText)).perform(ViewActions.clearText());
        Espresso.onView(withId(R.id.userCreateEmailEditText)).perform(ViewActions.clearText());
        Espresso.onView(withId(R.id.userCreatePasswordEditText)).perform(ViewActions.clearText());

        // user click the create account button
        Espresso.onView(withId(R.id.userCreateAccountButton)).perform(ViewActions.click());
        // the page should not move to another page
        Intents.assertNoUnverifiedIntents();
    }

    // agus raditya wibowo E2300106 testing "testClickCreateAccountButton()"
    // test on create account page if user click on login button the page should
    // be move to login page.
    @Test
    public void testClickLoginButton(){
        // user click the button
        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());

        // the page move to login page
        Intents.intended(IntentMatchers.hasComponent("com.example.zyra.studentFragmentAndActivity.login_activity"));
    }


}
