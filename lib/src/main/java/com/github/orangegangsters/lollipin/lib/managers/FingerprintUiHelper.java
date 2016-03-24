/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.github.orangegangsters.lollipin.lib.managers;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.orangegangsters.lollipin.lib.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Small helper class to manage
 * - cipher keys generation and use
 * - text/icon around fingerprint authentication UI.
 */
@TargetApi(Build.VERSION_CODES.M)
public class FingerprintUiHelper extends FingerprintManager.AuthenticationCallback {

    /**
     * The timeout for the error to be displayed. Returns to the normal UI after this.
     */
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    /**
     * The timeout for the success to be displayed. Calls {@link Callback#onAuthenticated()} after this.
     */
    private static final long SUCCESS_DELAY_MILLIS = 1300;
    /**
     * Alias for our key in the Android Key Store
     **/
    private static final String KEY_NAME = "my_key";

    /**
     * The {@link Cipher} used to init {@link FingerprintManager}
     */
    private Cipher mCipher;
    /**
     * The {@link KeyStore} used to initiliaze the key {@link #KEY_NAME}
     */
    private KeyStore mKeyStore;
    /**
     * The {@link KeyGenerator} used to generate the key {@link #KEY_NAME}
     */
    private KeyGenerator mKeyGenerator;
    /**
     * The {@link android.hardware.fingerprint.FingerprintManager.CryptoObject}
     */
    private final FingerprintManager mFingerprintManager;
    /**
     * The {@link ImageView} that is used to show the authent state
     */
    private final ImageView mIcon;
    /**
     * The {@link TextView} that is used to show the authent state
     */
    private final TextView mErrorTextView;
    /**
     * The {@link com.github.orangegangsters.lollipin.lib.managers.FingerprintUiHelper.Callback} used to return success or error.
     */
    private final Callback mCallback;
    /**
     * The {@link CancellationSignal} used after an error happens
     */
    private CancellationSignal mCancellationSignal;
    /**
     * Used if the user cancelled the authentication by himself
     */
    private boolean mSelfCancelled;

    /**
     * Builder class for {@link FingerprintUiHelper} in which injected fields from Dagger
     * holds its fields and takes other arguments in the {@link #build} method.
     */
    public static class FingerprintUiHelperBuilder {
        private final FingerprintManager mFingerPrintManager;

        public FingerprintUiHelperBuilder(FingerprintManager fingerprintManager) {
            mFingerPrintManager = fingerprintManager;
        }

        public FingerprintUiHelper build(ImageView icon, TextView errorTextView, Callback callback) {
            return new FingerprintUiHelper(mFingerPrintManager, icon, errorTextView,
                    callback);
        }
    }

    /**
     * Constructor for {@link FingerprintUiHelper}. This method is expected to be called from
     * only the {@link FingerprintUiHelperBuilder} class.
     */
    private FingerprintUiHelper(FingerprintManager fingerprintManager,
                                ImageView icon, TextView errorTextView, Callback callback) {
        mFingerprintManager = fingerprintManager;
        mIcon = icon;
        mErrorTextView = errorTextView;
        mCallback = callback;
    }

    /**
     * Starts listening to {@link FingerprintManager}
     *
     * @throws SecurityException If the hardware is not available, or the permission are not set
     */
    public void startListening() throws SecurityException {
        if (initCipher()) {
            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(mCipher);
            if (!isFingerprintAuthAvailable()) {
                return;
            }
            mCancellationSignal = new CancellationSignal();
            mSelfCancelled = false;
            mFingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
            mIcon.setImageResource(R.drawable.ic_fp_40px);
        }
    }

    /**
     * Stops listening to {@link FingerprintManager}
     */
    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    /**
     * Called by {@link FingerprintManager} if the authentication threw an error.
     */
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!mSelfCancelled) {
            showError(errString);
            mIcon.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError();
                }
            }, ERROR_TIMEOUT_MILLIS);
        }
    }

    /**
     * Called by {@link FingerprintManager} if the user asked for help.
     */
    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        showError(helpString);
    }

    /**
     * Called by {@link FingerprintManager} if the authentication failed (bad finger etc...).
     */
    @Override
    public void onAuthenticationFailed() {
        showError(mIcon.getResources().getString(
                R.string.pin_code_fingerprint_not_recognized));
    }

    /**
     * Called by {@link FingerprintManager} if the authentication succeeded.
     */
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mIcon.setImageResource(R.drawable.ic_fingerprint_success);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.success_color, null));
        mErrorTextView.setText(
                mErrorTextView.getResources().getString(R.string.pin_code_fingerprint_success));
        mIcon.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCallback.onAuthenticated();
            }
        }, SUCCESS_DELAY_MILLIS);
    }

    /**
     * Tells if the {@link FingerprintManager#isHardwareDetected()}, {@link FingerprintManager#hasEnrolledFingerprints()},
     * and {@link KeyguardManager#isDeviceSecure()}
     * 
     * @return true if yes, false otherwise
     * @throws SecurityException If the hardware is not available, or the permission are not set
     */
    public boolean isFingerprintAuthAvailable() throws SecurityException {
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints()
                && ((KeyguardManager) mIcon.getContext().getSystemService(Context.KEYGUARD_SERVICE)).isDeviceSecure();
    }

    /**
     * Initialize the {@link Cipher} instance with the created key in the {@link #createKey()}
     * method.
     *
     * @return {@code true} if initialization is successful, {@code false} if the lock screen has
     * been disabled or reset after the key was generated, or if a fingerprint got enrolled after
     * the key was generated.
     */
    private boolean initCipher() {
        try {
            if (mKeyStore == null) {
                mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            }
            createKey();
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);
            mCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (NoSuchPaddingException | KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            return false;
        }
    }

    /**
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     */
    public void createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            mKeyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            mKeyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            // Require the user to authenticate with a fingerprint to authorize every use
                            // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            mKeyGenerator.generateKey();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Show an error on the UI using {@link #mIcon} and {@link #mErrorTextView}
     */
    private void showError(CharSequence error) {
        mIcon.setImageResource(R.drawable.ic_fingerprint_error);
        mErrorTextView.setText(error);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.warning_color, null));
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }

    /**
     * Run by {@link #showError(CharSequence)} with delay to reset the original UI after an error.
     */
    Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.hint_color, null));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.pin_code_fingerprint_text));
            mIcon.setImageResource(R.drawable.ic_fp_40px);
        }
    };

    /**
     * The interface used to call the original Activity/Fragment... that uses this helper.
     */
    public interface Callback {
        void onAuthenticated();

        void onError();
    }
}
