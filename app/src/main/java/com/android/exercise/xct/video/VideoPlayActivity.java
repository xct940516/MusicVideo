package com.android.exercise.xct.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.exercise.xct.BaseActivity;
import com.android.exercise.xct.R;

public class VideoPlayActivity extends BaseActivity {


    private VideoView vv_video;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleBarvisibility(View.GONE);
        vv_video=(VideoView) findViewById(R.id.vv_videoplay);

        uri=getIntent().getData();
        vv_video.setVideoURI(uri);

        vv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_video.start();
            }
        });
        vv_video.setMediaController(new MediaController(this));

    }

    @Override
    public View setContentView() {
        return View.inflate(this, R.layout.activity_video_play,null);

    }

    @Override
    public void leftButtonClick() {

    }

    @Override
    public void rightButtonClick() {

    }
}
