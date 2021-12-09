package ovh.daxhelet.potagenial;

import androidx.test.espresso.Espresso;
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