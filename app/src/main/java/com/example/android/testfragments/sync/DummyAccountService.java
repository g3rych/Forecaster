package com.example.android.testfragments.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class DummyAccountService extends Service {
    private static final String TAG = DummyAccountService.class.getSimpleName();
    private static final String ACCOUNT_TYPE = "com.example.android.testfragments";
    private static final String ACCOUNT_NAME = "sync";
    private Authenticator mAuthenticator;
    @Override
    public void onCreate() {
        Log.d(TAG,"service created");
        mAuthenticator = new Authenticator(this);
    }

    public static Account getAccount() {
        return new Account(ACCOUNT_NAME,ACCOUNT_TYPE);
    }


    @Override
    public IBinder onBind(Intent intent) {
       return mAuthenticator.getIBinder();
    }

    public class Authenticator extends AbstractAccountAuthenticator {
        public Authenticator(Context context) {
            super(context);
        }
        @Override
        public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                     String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                 String s, String s2, String[] strings, Bundle bundle)
                throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                         Account account, Bundle bundle)
                throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                   Account account, String s, Bundle bundle)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthTokenLabel(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                        Account account, String s, Bundle bundle)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                  Account account, String[] strings)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }
    }




    }

