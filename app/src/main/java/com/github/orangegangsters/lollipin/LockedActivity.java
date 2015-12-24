package com.github.orangegangsters.lollipin;

import android.os.Bundle;

import com.github.orangegangsters.lollipin.lib.PinActivity;

import lollipin.orangegangsters.github.com.lollipin.R;

/**
 * Created by oliviergoutay on 1/13/15.
 */
public class LockedActivity extends PinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked);
    }

}
