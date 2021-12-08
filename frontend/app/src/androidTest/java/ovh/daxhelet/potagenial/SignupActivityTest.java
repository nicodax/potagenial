package ovh.daxhelet.potagenial;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.widget.EditText;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

public class SignupActivityTest {
    @Rule
    public ActivityTestRule<SignupActivity>signupActivityActivityTestRule=new ActivityTestRule<SignupActivity>(SignupActivity.class);

    private String eUsername = "jac00k";
    private String eEmail = "jack0000@hotmail.com";
    private String ePassword = "coucou";
    private String eConfirmPassword = "coucou";
    private String eFirstname = "jack";
    private String eLastname = "HJ";

    @Before
    public void setUp() throws Exception {
    }
    @Test
    public  void userInput() {
        Espresso.onView(withId(R.id.etSignupUsername)).perform(typeText(eUsername));
        Espresso.onView(withId(R.id.etSignupEmail)).perform(typeText(eEmail));
        Espresso.onView(withId(R.id.etSignupPassword)).perform(typeText(ePassword));
        Espresso.onView(withId(R.id.etSignupConfirmPassword)).perform(typeText(eConfirmPassword));
        Espresso.onView(withId(R.id.etLastname)).perform(typeText(eLastname));
        Espresso.onView(withId(R.id.etFirstname)).perform(typeText(eFirstname));
        Espresso.onView(withId(R.id.btnSignup)).perform(click());
    }


    @After
    public void tearDown() throws Exception {
    }
}