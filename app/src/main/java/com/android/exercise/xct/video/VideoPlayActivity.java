package com.android.exercise.xct.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.exercise.xct.BaseActivity;
import com.android.exercise.xct.R;
import com.android.exercise.xct.Util.Util;

public class VideoPlayActivity extends BaseActivity {

    private TextView tv_play_titile;
    private ImageView iv_battary;
    private TextView tv_system_time;
    private Button btn_voice;
    private SeekBar seekbar_voice;
    private Button btn_play_switch;
    private TextView tv_current_time;
    private SeekBar seekbar_play_time;
    private TextView tv_totle_time;
    private Button btn_play_exit;
    private Button btn_play_pre;
    private Button btn_play_pause;
    private Button btn_play_next;
    private Button btn_play_full;
    //电量值 0~100
    private  int level;
    private BroadcastReceiver MyBroadcastReceiver;
    private String TAG="VideoPlayActivity";
    //播放消息
    protected static  final int PROGRESS=1;

    /**
     * 视屏是否播放
     * true:正在播放
     * false:否
     */
    private Boolean isplaying=false;
    private VideoView vv_video;
    private Uri uri;
    private Util utils;
    //activity是否消耗标志
    private  Boolean isDestroy=false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
          switch (msg.what){
              case PROGRESS:

                  setBattery();
                  //设置系统时间
                  tv_system_time.setText(utils.getSystemTime());
                  int currentPositon= vv_video.getCurrentPosition();
                  Log.d("xiechangtao","currentPositon:"+currentPositon);
                  tv_current_time.setText(utils.stringForTime(currentPositon));
                    seekbar_play_time.setProgress(currentPositon);
                    //死循环消息
                if(!isDestroy){
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS,1000);
                }

                  break;
                default:
                    break;
          }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDate();
        initView();
        uri = getIntent().getData();
        vv_video.setVideoURI(uri);

        setListener();

    }

    private void initDate() {
        utils=new Util();
        isDestroy=false;

        //注册电量变化广播
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        MyBroadcastReceiver=new MyBroadcastReceiver();
        registerReceiver(MyBroadcastReceiver,filter);


    }

    /**
     * 控件的监听
     */
    private void setListener() {
        btn_play_pause.setOnClickListener(mOnClickListener);
        /**
         * 视屏拖动
         */
        seekbar_play_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Log.d("xiechangtao","seekTo(progress)："+progress);
                    vv_video.seekTo(progress);
                }
            }
            //手指开始拖动回调此方法
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //手指离开时回调此方法
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        vv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_video.start();
                isplaying=true;
            //得到视屏总时间
                int totletime=vv_video.getDuration();
                tv_totle_time.setText(utils.stringForTime(totletime));
                //设置播放进度条最大。将总时间与进度条关联起来
                seekbar_play_time.setMax(totletime);
            //发消息更新时间
                handler.sendEmptyMessage(PROGRESS);
            }
        });
           vv_video.setMediaController(new MediaController(this));//去掉系统自带的控制器
    }

    //设置电量图片
    private void setBattery(){
        Log.d(TAG,"level:"+level);
        if(level<=0){
            iv_battary.setImageResource(R.mipmap.ic_battery_0);
        }else if(level<=10){
            iv_battary.setImageResource(R.mipmap.ic_battery_10);
        }else if(level<=20){
            iv_battary.setImageResource(R.mipmap.ic_battery_20);

        }else if(level<=40){
            iv_battary.setImageResource(R.mipmap.ic_battery_40);

        }else if(level<=60){
            iv_battary.setImageResource(R.mipmap.ic_battery_60);

        }else  if(level<=80){
            iv_battary.setImageResource(R.mipmap.ic_battery_80);

        }else if(level<=100){
            iv_battary.setImageResource(R.mipmap.ic_battery_100);

        }

    }

    private class MyBroadcastReceiver  extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

               level= intent.getIntExtra("level",0);

          //  handler.sendEmptyMessage(PROGRESS);  应该发另外一个消息去处理电量。后期优化吧
        }
    }

    /**
     * 初始化
     */
    private  void initView(){
        setTitleBarvisibility(View.GONE);
        vv_video = (VideoView) findViewById(R.id.vv_videoplay);

        tv_play_titile=(TextView) findViewById(R.id.tv_play_titile);
        iv_battary=(ImageView) findViewById(R.id.iv_battary);
        btn_voice=(Button) findViewById(R.id.btn_voice);
        tv_system_time=(TextView) findViewById(R.id.tv_system_time);
        seekbar_voice=(SeekBar) findViewById(R.id.seekbar_voice);
        btn_play_switch=(Button) findViewById(R.id.btn_play_switch);
        tv_current_time=(TextView) findViewById(R.id.tv_current_time);
        seekbar_play_time=(SeekBar) findViewById(R.id.seekbar_play_time);
        tv_totle_time=(TextView) findViewById(R.id.tv_totle_time);
        btn_play_exit=(Button) findViewById(R.id.btn_play_exit);
        btn_play_pre=(Button) findViewById(R.id.btn_play_pre);
        btn_play_pause=(Button) findViewById(R.id.btn_play_pause);
        btn_play_next=(Button) findViewById(R.id.btn_play_next);
        btn_play_full=(Button) findViewById(R.id.btn_play_full);


    }

        //控件的监听
    View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_play_pause:
                        if(isplaying){
                            vv_video.pause();
                            btn_play_pause.setBackgroundResource(R.drawable.btn_play_selector);

                        }else{
                            vv_video.start();
                            btn_play_pause.setBackgroundResource(R.drawable.btn_play_pause_selector);
                        }
                        isplaying=!isplaying;

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy=true;
        unregisterReceiver(MyBroadcastReceiver);
        MyBroadcastReceiver=null;
    }

    @Override
    public View setContentView() {
        return View.inflate(this, R.layout.activity_video_play, null);

    }

    @Override
    public void leftButtonClick() {

    }

    @Override
    public void rightButtonClick() {

    }
}
