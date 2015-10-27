package lollipin.orangegangsters.github.com.lollipin.functional;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.github.orangegangsters.lollipin.MainActivity;
import com.robotium.solo.Solo;

/**
 * Created by stoyan and oliviergoutay on 1/13/15.
 */
public class AbstractTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String PASSWORD_PREFERENCE_KEY = "PASSCODE";
    private static final String LAST_ACTIVE_MILLIS_PREFERENCE_KEY = "LAST_ACTIVE_MILLIS";
    private static final String TIMEOUT_MILLIS_PREFERENCE_KEY = "TIMEOUT_MILLIS_PREFERENCE_KEY";
    private static final String LOGO_ID_PREFERENCE_KEY = "LOGO_ID_PREFERENCE_KEY";

    protected Solo solo;

    public AbstractTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        removeAllPrefs();
        solo.finishOpenedActivities();
    }

    /**
     * Click on the specified view id
     *
     * @param id
     */
    protected void clickOnView(int id) {
        solo.sleep(300);
        solo.clickOnView(getView(id));
    }

    /**
     * Get the view for the specified id
     *
     * @param id
     * @return
     */
    protected View getView(int id) {
        return solo.getView(id);
    }

    protected void removeAllPrefs() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(PASSWORD_PREFERENCE_KEY)
                .remove(LAST_ACTIVE_MILLIS_PREFERENCE_KEY)
                .remove(TIMEOUT_MILLIS_PREFERENCE_KEY)
                .remove(LOGO_ID_PREFERENCE_KEY)
                .apply();
    }

    protected void setMillis(long millis) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LAST_ACTIVE_MILLIS_PREFERENCE_KEY, millis);
        editor.apply();
    }
}
