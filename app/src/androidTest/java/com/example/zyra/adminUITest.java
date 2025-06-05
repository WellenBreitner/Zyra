package com.example.zyra;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zyra.adminFragmentAndActivity.admin_add_class_activity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class adminUITest {
    @Rule
    public IntentsTestRule<admin_add_class_activity> adminAddClassTest =
            new IntentsTestRule<>(admin_add_class_activity.class);

    //note the test should be done one by one

    // wellen breitner E2300128 testing "testPickPhoto()"
    // when user click the add photo lecturer button then
    // the intent should move to page that allow user select a photo
    @Test
    public void testPickPhoto() {
        onView(ViewMatchers.withId(R.id.addLecturePhotoPicker)).perform(click());
        intending(hasAction(Intent.ACTION_GET_CONTENT));
    }

    // agus raditya wibowo E2300106 testing "testAddClassNameTest()"
    // if user only input the class name and ignore all the text field
    // add class shouldn't can be accomplish because adding the class should
    // fill all the text field
    @Test
    public void testAddClassNameTest(){
        onView(ViewMatchers.withId(R.id.addClassNameEditText)).perform(typeText("testClass"));

        onView(ViewMatchers.withId(R.id.addNewClassButton)).perform(click());
        Intents.assertNoUnverifiedIntents();

    }

}
