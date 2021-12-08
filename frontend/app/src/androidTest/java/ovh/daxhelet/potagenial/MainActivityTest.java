package ovh.daxhelet.potagenial;

//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

//import android.app.Activity;
//import android.app.Instrumentation;
import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.Button;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    @Rule
    //Création d'une règle pour lancer notre activity
    public ActivityTestRule<MainActivity>mainActivityActivityTestRule=new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;
    public IntentsTestRule<MainActivity> intentsTestRule=new IntentsTestRule<>(MainActivity.class);

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(AideActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchLogo(){
        View logo = mainActivity.findViewById(R.id.ivLogo);
        assertNotNull(logo);
        //Espresso.onView(withId(R.id.ivLogo)).perform(ViewAction);

    }
    @Test
    public void testLaunchAide(){
        View aide = mainActivity.findViewById(R.id.ivAide);
        assertNotNull(aide);
        //onView(ViewMatchers.withId(R.id.ivAide)).perform(click());
        //intended(hasComponent(AideActivity.class.getName()));
        //Activity aideActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 1000);

        //aideActivity.finish();
    }

    @Test
    public void testLaunchCamera(){
        View camera = mainActivity.findViewById(R.id.ivCamera);
        assertNotNull(camera);
    }
    @Test
    public void testLaunchService(){
        View service = mainActivity.findViewById(R.id.ivServices);
        assertNotNull(service);
    }

    @Test
    public void testLaunchPanier(){
        View panier = mainActivity.findViewById(R.id.ivPanier);
        assertNotNull(panier);
    }

    @Test
    public void testLaunchProfil(){
        View profil = mainActivity.findViewById(R.id.ivProfil);
        assertNotNull(profil);
    }

    @Test
    public void testLaunchParametres(){
        View parametres = mainActivity.findViewById(R.id.ivParametres);
        assertNotNull(parametres);
    }


    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}