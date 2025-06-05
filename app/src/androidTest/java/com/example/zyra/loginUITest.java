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

import com.example.zyra.studentFragmentAndActivity.login_activity;

@RunWith(AndroidJUnit4.class)
public class loginUITest {

    @Rule
    public IntentsTestRule<login_activity> loginTest =
            new IntentsTestRule<>(login_activity.class);

    //note the test should be done one by one

    // i made bagus brian anjuna E2300116 testing "testEmptyFieldsLogin()"
    // test if the all the login text field is empty and user click a login button
    // the login page shouldn't move to another page.
    @Test
    public void testEmptyFieldsLogin() {
        // user enter empty value to email, and password
        Espresso.onView(withId(R.id.userLoginEmailEditText)).perform(ViewActions.clearText());
        Espresso.onView(withId(R.id.userLoginPasswordEditText)).perform(ViewActions.clearText());

        // user click the login button
        Espresso.onView(withId(R.id.userLoginButton)).perform(ViewActions.click());

        // the page should not move to another page
        Intents.assertNoUnverifiedIntents();
    }

    // i made bagus brian anjuna E2300116 testing "testClickCreateAccountButton()"
    // test on login page if user click on create account button the page should
    // be move to create account page
    @Test
    public void testClickCreateAccountButton(){
        Espresso.onView(withId(R.id.button_signup)).perform(ViewActions.click());

        Intents.intended(IntentMatchers.hasComponent("com.example.zyra.studentFragmentAndActivity.create_account_activity"));
    }

}
