package com.android.exercise.xct;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

public class SplashActivity extends Activity {

    private  boolean isEnterMain = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /**
         * 等待两秒再进入主页面
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterMain();
            }
        }, 2000);

    }

    public void enterMain(){
        if(!isEnterMain){
            isEnterMain=true;
            Intent intent=new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    /**
     * true :已经进入主页面
     * false： 还没进入主页面
     */


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        enterMain();
        return super.onTouchEvent(event);
    }
}
