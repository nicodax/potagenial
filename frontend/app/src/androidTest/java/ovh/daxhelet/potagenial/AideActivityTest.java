package ovh.daxhelet.potagenial;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;

import android.app.Instrumentation;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AideActivityTest {

    @Rule
    public IntentsTestRule<AideActivity> aideActivityIntentsTestRule = new IntentsTestRule<>(AideActivity.class);


    @Before
    public void setUp() throws Exception {
    }

    @Test

    public void sendEmailTest(){
        Espresso.onView(ViewMatchers.withId(R.id.bouton)).perform(ViewActions.click());
    }


    @After
    public void tearDown() throws Exception {
    }
}