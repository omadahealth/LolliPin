package lollipin.orangegangsters.github.com.lollipin.functional;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.omadahealth.lollipin.CustomPinActivity;
import com.github.omadahealth.lollipin.MainActivity;
import com.github.omadahealth.lollipin.NotLockedActivity;
import com.github.omadahealth.lollipin.lib.encryption.Encryptor;
import com.github.omadahealth.lollipin.lib.enums.Algorithm;
import com.github.omadahealth.lollipin.lib.managers.AppLockImpl;
import com.github.omadahealth.lollipin.lib.managers.FingerprintUiHelper;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.github.omadahealth.lollipin.lib.views.PinCodeRoundView;

import lollipin.orangegangsters.github.com.lollipin.R;

/**
 * @author stoyan and oliviergoutay
 * @version 1/13/15
 */
public class PinLockTest extends AbstractTest {

    public void testMigratingFromSha1toSha256() {
        //Init
        removeAllPrefs();
        AppLockImpl appLockImpl = (AppLockImpl) LockManager.getInstance().getAppLock();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Should use sha256 if the SharedPreferences is set, by default
        enablePin();
        assertEquals(Algorithm.SHA256, Algorithm.getFromText(sharedPref.getString(PASSWORD_ALGORITHM_PREFERENCE_KEY, "")));
        assertTrue(appLockImpl.checkPasscode("1234"));
        removeAllPrefs();

        //Should still use sha1 if password is stored but not the algorithm
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PASSWORD_PREFERENCE_KEY, Encryptor.getSHA(appLockImpl.getSalt() + "test" + appLockImpl.getSalt(), Algorithm.SHA1));
        editor.apply();
        assertEquals(Algorithm.SHA1, Algorithm.getFromText(sharedPref.getString(PASSWORD_ALGORITHM_PREFERENCE_KEY, "")));
        assertTrue(appLockImpl.checkPasscode("test"));
        removeAllPrefs();
    }

    public void testPinClearButton() {
        removePrefsAndGoToEnable();

        //Enter 3 codes
        clickOnView(R.id.pin_code_button_1);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);

        //Check length 3
        solo.sleep(1000);
        PinCodeRoundView pinCodeRoundView = (PinCodeRoundView) solo.getCurrentActivity().findViewById(com.github.omadahealth.lollipin.lib.R.id.pin_code_round_view);
        assertEquals(3, pinCodeRoundView.getCurrentLength());

        //Click clear button
        clickOnView(R.id.pin_code_button_clear);

        //Check length 0
        solo.sleep(1000);
        assertEquals(2, pinCodeRoundView.getCurrentLength());
    }

    public void testPinEnabling() {
        removePrefsAndGoToEnable();

        //Test no fingerprint
        assertEquals(View.GONE, solo.getView(R.id.pin_code_fingerprint_imageview).getVisibility());
        assertEquals(View.GONE, solo.getView(R.id.pin_code_fingerprint_textview).getVisibility());

        //--------Not the same pin--------
        //Enter 4 codes
        clickOnView(R.id.pin_code_button_1);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        solo.sleep(1000);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        clickOnView(R.id.pin_code_button_5);
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);
        solo.sleep(1000);

        //--------Same pin--------
        enablePin();
    }

    public void testPinEnablingChecking() throws SecurityException {
        enablePin();

        //Go to unlock
        clickOnView(R.id.button_unlock_pin);
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);

        //Test fingerprint if available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ImageView fingerprintImageView = (ImageView) solo.getView(com.github.omadahealth.lollipin.lib.R.id.pin_code_fingerprint_imageview);
            TextView fingerprintTextView = (TextView) solo.getView(com.github.omadahealth.lollipin.lib.R.id.pin_code_fingerprint_textview);
            FingerprintManager fingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
            FingerprintUiHelper fingerprintUiHelper = new FingerprintUiHelper.FingerprintUiHelperBuilder(fingerprintManager).build(fingerprintImageView, fingerprintTextView, (CustomPinActivity) solo.getCurrentActivity());
            if (fingerprintManager.isHardwareDetected() && fingerprintUiHelper.isFingerprintAuthAvailable()) {
                assertEquals(View.VISIBLE, solo.getView(R.id.pin_code_fingerprint_imageview).getVisibility());
                assertEquals(View.VISIBLE, solo.getView(R.id.pin_code_fingerprint_textview).getVisibility());
            } else {
                assertEquals(View.GONE, solo.getView(R.id.pin_code_fingerprint_imageview).getVisibility());
                assertEquals(View.GONE, solo.getView(R.id.pin_code_fingerprint_textview).getVisibility());
            }
        } else {
            assertEquals(View.GONE, solo.getView(R.id.pin_code_fingerprint_imageview).getVisibility());
            assertEquals(View.GONE, solo.getView(R.id.pin_code_fingerprint_textview).getVisibility());
        }

        //Enter the code
        clickOnView(R.id.pin_code_button_1);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);

        //Check view
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("MainActivity", MainActivity.class);
    }

    public void testPinEnablingChanging() {
        enablePin();

        //Go to change
        clickOnView(R.id.button_change_pin);
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);

        //Enter previous code
        clickOnView(R.id.pin_code_button_1);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        solo.sleep(1000);

        //Enter the new one
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        clickOnView(R.id.pin_code_button_5);
        solo.sleep(1000);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        clickOnView(R.id.pin_code_button_5);
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("MainActivity", MainActivity.class);

        //Go to unlock
        clickOnView(R.id.button_unlock_pin);
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);

        //Enter the code
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        clickOnView(R.id.pin_code_button_5);

        //Check view
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("MainActivity", MainActivity.class);
    }

    public void testPinLockAfterDefaultTimeout() {
        enablePin();

        //Go to NotLockedActivity
        solo.sleep(1000);
        clickOnView(R.id.button_not_locked);
        solo.waitForActivity(NotLockedActivity.class);
        solo.assertCurrentActivity("NotLockedActivity", NotLockedActivity.class);

        //Set the last time to now - 11sec
        setMillis(System.currentTimeMillis() - (1000 * 15));
        solo.getCurrentActivity().finish();

        //Check view
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);
        solo.sleep(1000);
    }

    public void testPinLockAfterCustomTimeout() {
        enablePin();

        //Set to 3minutes
        LockManager.getInstance().getAppLock().setTimeout(1000 * 60 * 3);

        //Go to NotLockedActivity
        clickOnView(R.id.button_not_locked);
        solo.waitForActivity(NotLockedActivity.class);
        solo.assertCurrentActivity("NotLockedActivity", NotLockedActivity.class);

        //Set the last time to now - 11sec
        setMillis(System.currentTimeMillis() - (1000 * 11));
        solo.getCurrentActivity().finish();

        //Check view
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("MainActivity", MainActivity.class);
        solo.sleep(1000);

        //Go to NotLockedActivity
        clickOnView(R.id.button_not_locked);
        solo.waitForActivity(NotLockedActivity.class);
        solo.assertCurrentActivity("NotLockedActivity", NotLockedActivity.class);

        //Set the last time to now - 6minutes
        setMillis(System.currentTimeMillis() - (1000 * 60 * 6));
        solo.getCurrentActivity().finish();

        //Check view
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);
        solo.sleep(1000);
    }

    public void testPinLockWithBackgroundTimeout() {
        enablePin();

        // Set the option to use timeout in background only
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ONLY_BACKGROUND_TIMEOUT_PREFERENCE_KEY, true);
        editor.apply();

        //Go to NotLockedActivity
        solo.sleep(1000);
        clickOnView(R.id.button_not_locked);
        solo.waitForActivity(NotLockedActivity.class);
        solo.assertCurrentActivity("NotLockedActivity", NotLockedActivity.class);

        //Set the last time to now - 15sec
        setMillis(System.currentTimeMillis() - (1000 * 15));
        solo.getCurrentActivity().finish();

        //Check view
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("MainActivity", MainActivity.class);
        solo.sleep(1000);
    }

    public void testBackButton() {
        enablePin();

        //Go to unlock
        clickOnView(R.id.button_unlock_pin);
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);

        //reset
        clickOnView(R.id.pin_code_button_1);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        solo.sleep(1000);

        //Go to change
        clickOnView(R.id.button_change_pin);
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("MainActivity", MainActivity.class);
    }

    public void testDisablingFingerprintReader() {
        enablePin();

        // Disable fingerprint reader.
        LockManager.getInstance().getAppLock().setFingerprintAuthEnabled(false);

        // Go to unlock.
        clickOnView(R.id.button_unlock_pin);
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);

        // Make sure the fingerprint views are gone.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            assertEquals(View.GONE, solo.getView(R.id.pin_code_fingerprint_imageview).getVisibility());
            assertEquals(View.GONE, solo.getView(R.id.pin_code_fingerprint_textview).getVisibility());
        }

        // Make sure pin unlocking still works.
        clickOnView(R.id.pin_code_button_1);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("MainActivity", MainActivity.class);
    }

    private void enablePin() {
        removePrefsAndGoToEnable();

        clickOnView(R.id.pin_code_button_1);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        solo.sleep(1000);
        clickOnView(R.id.pin_code_button_1);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);
        clickOnView(R.id.pin_code_button_4);
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("MainActivity", MainActivity.class);
    }

    private void removePrefsAndGoToEnable() {
        //init
        removeAllPrefs();

        //Go to enable
        if (solo.getCurrentActivity() instanceof MainActivity) {
            clickOnView(R.id.button_enable_pin);
            solo.waitForActivity(CustomPinActivity.class);
            solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);
            solo.waitForText("1");
        }
    }
}
