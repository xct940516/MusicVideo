package com.android.exercise.xct.video;

import android.annotation.SuppressLint;
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.exercise.xct.BaseActivity;
import com.android.exercise.xct.R;
import com.android.exercise.xct.Util.Util;
import com.android.exercise.xct.domain.VideoItemBean;

import java.util.ArrayList;

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
    private int level;
    private BroadcastReceiver MyBroadcastReceiver;
    private String TAG = "VideoPlayActivity";
    //1.定义手势识别器
    private GestureDetector detector;
    private LinearLayout ll_contral_play;
    //播放消息
    protected static final int PROGRESS = 1;
    //消息延迟隐藏控制栏
    protected static  final int HIDECONTROLPLAYER = 2;

    /**
     * 视屏是否播放的标志
     * true:正在播放
     * false:否
     */
    private Boolean isplaying = false;
    private VideoView vv_video;
    private Uri uri;
    private Util utils;
    //activity是否消耗的标志
    private Boolean isDestroy = false;
    private ArrayList<VideoItemBean> videoItemBeans;
    private int position;

    //ToDo 关于全局变量

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS:
                    //更新UI,后续将以下全部抽取成方法
                    setBattery();//设置电量标志
                    //设置系统时间
                    tv_system_time.setText(utils.getSystemTime());
                    //设置视屏的当前时间
                    int currentPositon = vv_video.getCurrentPosition();
                    Log.d(TAG, "currentPositon:" + currentPositon);
                    tv_current_time.setText(utils.stringForTime(currentPositon));
                    //设置进度条前进
                    seekbar_play_time.setProgress(currentPositon);
                    //死循环消息
                    if (!isDestroy) {
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    }

                    break;
                case  HIDECONTROLPLAYER:
                    hideContralPlayer();
                    break;
                default:
                    break;
            }
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDate();
        initView();
        getData();
        setData();
        setListener();

    }

    /**
     * 设置数据
     */
    private void setData() {
        if (videoItemBeans != null && videoItemBeans.size() > 0) {
            //当从视屏列表跳转时，得到整个视屏列表
            VideoItemBean videoItemBean = videoItemBeans.get(position);
            //设置播放地址
            vv_video.setVideoPath(videoItemBean.getData());
            //设置标题
            tv_play_titile.setText(videoItemBean.getTitle());
        } else if (uri != null) {
            //设置播放地址（当从第三方跳转时）
            vv_video.setVideoURI(uri);
        }


    }

    /**
     * 得到数据
     */

    private void getData() {
        //得到视屏列表对象（多个）
        videoItemBeans = (ArrayList<VideoItemBean>) getIntent().getSerializableExtra("videoItemBeans");//java自带序列化
        //getIntent().getParcelableExtra();     android 自带序列化(原理还是将数据拆分成一个一个intent，效率高，但不适合本地传递数据)
        //得到点击视屏位置
        position = getIntent().getIntExtra("position", 0);

        //得到带个数据
        uri = getIntent().getData();

    }

    private void initDate() {
        utils = new Util();
        isDestroy = false;

        //注册电量变化广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        MyBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(MyBroadcastReceiver, filter);

        //2.实例化手势识别器
        detector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            //手指长按
            @Override
            public void onLongPress(MotionEvent e) {
                Toast.makeText(getApplicationContext(),"长按",0).show();
                videoPlayOrPause();
                super.onLongPress(e);
            }
            //双击
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(getApplicationContext(),"双击",0).show();

                return super.onDoubleTap(e);
            }
            //单击
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Toast.makeText(getApplicationContext(),"单击",0).show();
                if(isShwoContral){
                    removeHideControlMessage();
                    hideContralPlayer();
                }else {
                    showContralPlayer();
                    sendHideControlMessage();
                }
                return super.onSingleTapConfirmed(e);
            }
        });

    }

    /**
     * 发送延迟消息隐藏控制栏
     */
    protected void sendHideControlMessage(){
        handler.sendEmptyMessageDelayed(HIDECONTROLPLAYER,5000);
    }

    /**
     * 发送消息移除隐藏的消息
     */

    protected  void removeHideControlMessage(){
        handler.removeMessages(HIDECONTROLPLAYER);
    }



    //使用手势识别,消耗它

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        detector.onTouchEvent(event);
        return true;
    }

    /**
     * 控件的监听
     */
    private void setListener() {
        btn_play_pause.setOnClickListener(mOnClickListener);
        btn_play_next.setOnClickListener(mOnClickListener);
        btn_play_pre.setOnClickListener(mOnClickListener);
        /**
         * 视屏进度条拖动的监听
         */
        seekbar_play_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Log.d(TAG, "seekTo(progress)：" + progress);
                    vv_video.seekTo(progress);
                }
            }


            //ToDo   要去了解消息发送和移除的根本
            //手指开始拖动回调此方法
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                removeHideControlMessage();//(5s后发的消息在消息队列中，移除它)
            }

            //手指离开时回调此方法
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendHideControlMessage();
            }
        });
        //mediaplayer生命周期  准备 的监听
        vv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                vv_video.start();
                isplaying = true;
                //得到视屏总时间，然后设置到控件中
                int totletime = vv_video.getDuration();
                tv_totle_time.setText(utils.stringForTime(totletime));
                //设置播放进度条最大。将总时间与进度条关联起来
                seekbar_play_time.setMax(totletime);

                //一进来就隐藏控制栏
                hideContralPlayer();

                //发消息更新时间
                handler.sendEmptyMessage(PROGRESS);
            }
        });
        //    vv_video.setMediaController(new MediaController(this));//去掉系统自带的控制器

        //设置播放完成的监听

        vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //1.有下一个视屏，播放完成后继续播放下一个
                // 2.没有下一个视屏，则退出
                playNextVideo();

            }
        });

    }

    /**
     * 播放下一个视屏
     */
    private void playNextVideo() {
        if (videoItemBeans != null && videoItemBeans.size() > 0) {
            position++;
            if (position < videoItemBeans.size()) {
                //有下一个视屏（播放第一个，播最后一个）
                VideoItemBean videoItemBean = videoItemBeans.get(position);

                vv_video.setVideoPath(videoItemBean.getData());

                tv_play_titile.setText(videoItemBean.getTitle());

                setPlayStatus();

            } else {
                position=videoItemBeans.size()-1;
                //越界，最后一个视屏播完走着。
                Toast.makeText(getApplicationContext(), "最后一个视屏播完了", 1).show();
                finish();
            }
        }
    }

    /**
     * 播放上一个视屏
     */
    private void playPreVideo() {
        if (videoItemBeans != null && videoItemBeans.size() > 0) {
            position--;
            if (position >=0) {
                //有上一个视屏
                VideoItemBean videoItemBean = videoItemBeans.get(position);
                vv_video.setVideoPath(videoItemBean.getData());
                tv_play_titile.setText(videoItemBean.getTitle());
                setPlayStatus();
            } else {
                //越界，这是一个视屏播完走着。
                position=0;
                Toast.makeText(getApplicationContext(), "这就是第一个视屏了", 1).show();
                finish();

            }

        }
    }

    /**
     * 设置视屏播放状态
     */
    private void setPlayStatus() {
//        Log.d(TAG,Log.getStackTraceString(new Throwable()));
        Exception exception=new Exception("call back");
        exception.printStackTrace();

        if (position == 0) {
            //第一个视屏 上一个按钮不能点，这个条件似乎怎么也不会走
            btn_play_pre.setBackgroundResource(R.mipmap.btn_pre_gray);
            btn_play_pre.setEnabled(false);
            

        } else if (position == videoItemBeans.size() - 1) {
            //最后一个是视屏，下一个按钮不能点
            btn_play_next.setBackgroundResource(R.mipmap.btn_next_gray);
            btn_play_next.setEnabled(false);
        } else {

            btn_play_pre.setBackgroundResource(R.drawable.btn_play_pre_selector);
            btn_play_pre.setEnabled(true);

            btn_play_next.setBackgroundResource(R.drawable.btn_play_next_selector);
            btn_play_next.setEnabled(true);

        }
    }

    //设置电量显示图片
    private void setBattery() {
        Log.d(TAG, "level:" + level);
        if (level <= 0) {
            iv_battary.setImageResource(R.mipmap.ic_battery_0);
        } else if (level <= 10) {
            iv_battary.setImageResource(R.mipmap.ic_battery_10);
        } else if (level <= 20) {
            iv_battary.setImageResource(R.mipmap.ic_battery_20);

        } else if (level <= 40) {
            iv_battary.setImageResource(R.mipmap.ic_battery_40);

        } else if (level <= 60) {
            iv_battary.setImageResource(R.mipmap.ic_battery_60);

        } else if (level <= 80) {
            iv_battary.setImageResource(R.mipmap.ic_battery_80);

        } else if (level <= 100) {
            iv_battary.setImageResource(R.mipmap.ic_battery_100);

        }

    }

    /**
     * 广播接收者监听电量改变的广播
     * level  0~100
     */

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

             String Action = intent.getAction();
            Log.d(TAG,"收到的广播是："+Action);
            level = intent.getIntExtra("level", 0);

            //  handler.sendEmptyMessage(PROGRESS);  应该发另外一个消息去处理电量。后期优化吧
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        setTitleBarvisibility(View.GONE);
        vv_video = (VideoView) findViewById(R.id.vv_videoplay);

        tv_play_titile = (TextView) findViewById(R.id.tv_play_titile);
        iv_battary = (ImageView) findViewById(R.id.iv_battary);
        btn_voice = (Button) findViewById(R.id.btn_voice);
        tv_system_time = (TextView) findViewById(R.id.tv_system_time);
        seekbar_voice = (SeekBar) findViewById(R.id.seekbar_voice);
        btn_play_switch = (Button) findViewById(R.id.btn_play_switch);
        tv_current_time = (TextView) findViewById(R.id.tv_current_time);
        seekbar_play_time = (SeekBar) findViewById(R.id.seekbar_play_time);
        tv_totle_time = (TextView) findViewById(R.id.tv_totle_time);
        btn_play_exit = (Button) findViewById(R.id.btn_play_exit);
        btn_play_pre = (Button) findViewById(R.id.btn_play_pre);
        btn_play_pause = (Button) findViewById(R.id.btn_play_pause);
        btn_play_next = (Button) findViewById(R.id.btn_play_next);
        btn_play_full = (Button) findViewById(R.id.btn_play_full);
        ll_contral_play= (LinearLayout) findViewById(R.id.ll_control_play);
    }

    //控件的监听
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_play_pause:
                    //视屏播放暂停键的处理
                    videoPlayOrPause();

                    break;

                case R.id.btn_play_next:
                    playNextVideo();
                    break;
                case R.id.btn_play_pre:
                    playPreVideo();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 视屏或者暂停
     */

    private void videoPlayOrPause() {
        if (isplaying) {
            vv_video.pause();
            btn_play_pause.setBackgroundResource(R.drawable.btn_play_selector);

        } else {
            vv_video.start();
            btn_play_pause.setBackgroundResource(R.drawable.btn_play_pause_selector);
        }
        isplaying = !isplaying;
    }

    /**
     * 控制栏是否显示
     */
    private Boolean isShwoContral=false;
        //隐藏控制栏
    private void hideContralPlayer(){
        ll_contral_play.setVisibility(View.GONE);
        isShwoContral=false;
    }

        //显示控制栏
    private void showContralPlayer(){
        ll_contral_play.setVisibility(View.VISIBLE);
        isShwoContral=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁后将销毁标志置为true
        isDestroy = true;
        //activity 销毁后将广播也取消注册并且置为空
        unregisterReceiver(MyBroadcastReceiver);
        MyBroadcastReceiver = null;
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
