package com.android.exercise.xt.video;

import android.os.Bundle;
import android.view.View;

import com.android.exercise.xct.BaseActivity;

public class videoListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRightvisibility(View.GONE);
    }

    @Override
    public View setContentView() {
        return null;
    }

    @Override
    public void leftButtonClick() {

    }

    @Override
    public void rightButtonClick() {
        finish();
    }


}
