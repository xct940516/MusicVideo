package com.android.exercise.xct;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLeftvisibility(View.GONE);
        setContentTitle("新闻标题");
    }


    public void leftButtonClick(){
        Toast.makeText(this,"点击了左边按钮",Toast.LENGTH_LONG).show();
    }

    public void rightButtonClick(){
        Toast.makeText(this,"点击了右边按钮",Toast.LENGTH_LONG).show();

    }


    @Override
    public View setContentView() {
        //把activity_main 转换成view对象
       return View.inflate(this,R.layout.activity_main,null);

    }
}
