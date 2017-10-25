package com.dewarder.realmstorage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.orangegangsters.lollipin.lib.enums.Algorithm;
import com.github.orangegangsters.lollipin.lib.interfaces.PasscodeDataStorage;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public final class RealmPasscodeDataStorage implements PasscodeDataStorage {

    private static final String REALM_NAME = "passcode_data.store";

    private final Realm mRealm;

    public RealmPasscodeDataStorage(Context context) {
        this(RealmEncryptionKeyManager.getKeyProvider(context));
    }

    public RealmPasscodeDataStorage(RealmEncryptionKeyProvider keyProvider) {
        mRealm = Realm.getInstance(createDefaultConfiguration(keyProvider));
    }

    public RealmPasscodeDataStorage(RealmConfiguration configuration) {
        mRealm = Realm.getInstance(configuration);
    }

    private static RealmConfiguration createDefaultConfiguration(RealmEncryptionKeyProvider keyProvider) {
        return new RealmConfiguration.Builder()
                .name(REALM_NAME)
                .encryptionKey(keyProvider.provideEncryptionKey())
                .modules(new PasscodeDataModule())
                .build();
    }

    @Override
    public int readAttemptsCount() {
        return getPasscodeData().getAttempts();
    }

    @Override
    public void writeAttemptsCount(int attempts) {
        mRealm.beginTransaction();
        PasscodeData data = getPasscodeData();
        data.setAttempts(attempts);
        mRealm.copyToRealmOrUpdate(data);
        mRealm.commitTransaction();
    }

    @Nullable
    @Override
    public String readSalt() {
        return getPasscodeData().getSalt();
    }

    @Override
    public void writeSalt(@Nullable String salt) {
        mRealm.beginTransaction();
        PasscodeData data = getPasscodeData();
        data.setSalt(salt);
        mRealm.copyToRealmOrUpdate(data);
        mRealm.commitTransaction();
    }

    @NonNull
    @Override
    public Algorithm readCurrentAlgorithm() {
        return Algorithm.getFromText(
                getPasscodeData().getAlgorithm());
    }

    @Override
    public void writeCurrentAlgorithm(@NonNull Algorithm algorithm) {
        mRealm.beginTransaction();
        PasscodeData data = getPasscodeData();
        data.setAlgorithm(algorithm.getValue());
        mRealm.copyToRealmOrUpdate(data);
        mRealm.commitTransaction();
    }

    @NonNull
    @Override
    public String readPasscode() {
        return getPasscodeData().getPasscode();
    }

    @Override
    public void writePasscode(@NonNull String passcode) {
        mRealm.beginTransaction();
        PasscodeData data = getPasscodeData();
        data.setPasscode(passcode);
        mRealm.copyToRealmOrUpdate(data);
        mRealm.commitTransaction();
    }

    @Override
    public boolean hasPasscode() {
        return getPasscodeData().getPasscode() != null;
    }

    @Override
    public void clearPasscode() {
        mRealm.beginTransaction();
        PasscodeData data = getPasscodeData();
        data.setPasscode(null);
        mRealm.copyToRealmOrUpdate(data);
        mRealm.commitTransaction();
    }

    @Override
    public void clear() {
        mRealm.beginTransaction();
        mRealm.delete(PasscodeData.class);
        mRealm.commitTransaction();
    }

    private PasscodeData getPasscodeData() {
        return mRealm.where(PasscodeData.class)
                .findAll()
                .first(new PasscodeData());
    }
}
