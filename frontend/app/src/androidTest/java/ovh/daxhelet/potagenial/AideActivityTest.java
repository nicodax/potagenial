package ovh.daxhelet.potagenial;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

public class AideActivityTest {

    @Rule
    public IntentsTestRule<AideActivity> aideActivityIntentsTestRule = new IntentsTestRule<>(AideActivity.class);

    @Test
    public void sendEmailTest(){

        //Espresso.onView(ViewMatchers.withId(R.id.bouton)).perform(ViewActions.click());
    }

}