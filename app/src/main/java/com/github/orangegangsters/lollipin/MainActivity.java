package com.github.orangegangsters.lollipin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.AppLockActivity;

import lollipin.orangegangsters.github.com.lollipin.R;


public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViewById(R.id.button_enable_pin).setOnClickListener(this);
        this.findViewById(R.id.button_change_pin).setOnClickListener(this);
        this.findViewById(R.id.button_unlock_pin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, AppLockActivity.class);
        switch (v.getId()) {
            case R.id.button_enable_pin:
                intent.putExtra(AppLock.TYPE, AppLock.ENABLE_PINLOCK);
                break;
            case R.id.button_change_pin:
                intent.putExtra(AppLock.TYPE, AppLock.CHANGE_PIN);
                break;
            case R.id.button_unlock_pin:
                intent.putExtra(AppLock.TYPE, AppLock.UNLOCK_PIN);
            default:
                break;
        }
        startActivity(intent);
    }
}
