package lollipin.orangegangsters.github.com.lollipin.functional;

import com.github.orangegangsters.lollipin.CustomPinActivity;
import com.github.orangegangsters.lollipin.MainActivity;
import com.github.orangegangsters.lollipin.NotLockedActivity;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.github.orangegangsters.lollipin.lib.views.PinCodeRoundView;

import lollipin.orangegangsters.github.com.lollipin.R;

/**
 * @author stoyan and oliviergoutay
 * @version 1/13/15
 */
public class PinLockTest extends AbstractTest {

    public void testPinClearButton() {
        removePrefsAndGoToEnable();

        //Enter 3 codes
        clickOnView(R.id.pin_code_button_1);
        clickOnView(R.id.pin_code_button_2);
        clickOnView(R.id.pin_code_button_3);

        //Check length 3
        solo.sleep(1000);
        PinCodeRoundView pinCodeRoundView = (PinCodeRoundView) solo.getCurrentActivity().findViewById(com.github.orangegangsters.lollipin.lib.R.id.pin_code_round_view);
        assertEquals(3, pinCodeRoundView.getCurrentLength());

        //Click clear button
        clickOnView(R.id.pin_code_button_clear);

        //Check length 0
        solo.sleep(1000);
        assertEquals(2, pinCodeRoundView.getCurrentLength());
    }

    public void testPinEnabling() {
        removePrefsAndGoToEnable();

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

    public void testPinEnablingChecking() {
        enablePin();

        //Go to unlock
        clickOnView(R.id.button_unlock_pin);
        solo.waitForActivity(CustomPinActivity.class);
        solo.assertCurrentActivity("CustomPinActivity", CustomPinActivity.class);

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

        //Set the last time to now - 4minutes
        setMillis(System.currentTimeMillis() - (1000 * 60 * 4));
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
