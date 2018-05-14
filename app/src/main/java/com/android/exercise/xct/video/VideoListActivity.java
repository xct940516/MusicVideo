package com.android.exercise.xct.video;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.exercise.xct.BaseActivity;
import com.android.exercise.xct.R;

public class VideoListActivity extends BaseActivity {

    private ListView lv_video;
    private TextView tv_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRightvisibility(View.GONE);
        setContentTitle("新闻");

        lv_video=findViewById(R.id.lv_video);
        tv_video=findViewById(R.id.tv_video);




    }

    @Override
    public View setContentView() {
        return View.inflate(this, R.layout.activity_video_list,null);
    }

    @Override
    public void leftButtonClick() {
        finish();
    }

    @Override
    public void rightButtonClick() {

    }
}
