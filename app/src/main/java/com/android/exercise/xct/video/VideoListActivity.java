package com.android.exercise.xct.video;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.exercise.xct.BaseActivity;
import com.android.exercise.xct.R;
import com.android.exercise.xct.Util.PermissionUtils;
import com.android.exercise.xct.Util.Util;
import com.android.exercise.xct.domain.VideoItemBean;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

public class VideoListActivity extends BaseActivity {

    private ListView lv_video;
    private TextView tv_video;
    private ArrayList<VideoItemBean> videoItemBeans;
    private Util util;
    private String TAG="VideoListActivity";
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(videoItemBeans!=null&&videoItemBeans.size()>0){
                tv_video.setVisibility(View.GONE);
                lv_video.setAdapter(new MyAdpter());

            }else {
                //隐藏没有视屏字符
                tv_video.setVisibility(View.VISIBLE);
            }

        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRightvisibility(View.GONE);
        setContentTitle("本地视屏");

        lv_video=(ListView) findViewById(R.id.lv_video);
        tv_video=(TextView) findViewById(R.id.tv_video);
        util=new Util();
        getAllVideo();

        lv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoItemBean videoItemBean=videoItemBeans.get(position);
              //  Toast.makeText(VideoListActivity.this,videoItemBean.getTitle(),Toast.LENGTH_LONG).show();
                //设置单个的数据传递
//                Intent intent=new Intent(VideoListActivity.this,VideoPlayActivity.class);
//                intent.setData(Uri.parse(videoItemBean.getData()));
//                startActivity(intent);
                //将整个视频列表传过去
                Intent intent=new Intent(VideoListActivity.this,VideoPlayActivity.class);
                Bundle bundle=new Bundle();
                //将视屏列表放入对象放入bundle中（对象需要序列化(java或者android自带的))
                //bundle.putParcelable();  android 自带的序列化
                bundle.putSerializable("videoItemBeans",videoItemBeans);//java 自带的序列化
                //传入视屏列表list
                intent.putExtras(bundle);
                //传入点击位置
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

    }

    private class  MyAdpter extends BaseAdapter{

        @Override
        public int getCount() {
            return videoItemBeans.size();
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //测试
//            TextView tv=new TextView(VideoListActivity.this);
//            tv.setText(videoItemBeans.get(i).toString());
//            tv.setTextColor(Color.WHITE);
//            tv.setTextSize(18);
            //ToDo  关于viewHold


              View v;
              ViewHolder holder;
          if(view!=null){
              v=view;
              holder=(ViewHolder) v.getTag();
          }else{
              v=View.inflate(VideoListActivity.this,R.layout.videolist_item,null);
              holder=new ViewHolder();
              holder.iv_videoname=(TextView) v.findViewById(R.id.iv_videoname);
              holder.iv_videoduration=(TextView) v.findViewById(R.id.iv_videoduration);
              holder.iv_videosize=(TextView) v.findViewById(R.id.iv_videosize);
                v.setTag(holder);
          }

            VideoItemBean videoItemBean=videoItemBeans.get(i);
            holder.iv_videoname.setText(videoItemBean.getTitle());
            //将时长，和视屏大小转换
            Log.d(TAG,"第"+i+"个视屏时长："+videoItemBean.getDuration());
            //将string转换为int   再格式化
            holder.iv_videoduration.setText( util.stringForTime(Integer.valueOf(videoItemBean.getDuration())));

            holder.iv_videosize.setText(android.text.format.Formatter.formatFileSize(VideoListActivity.this,videoItemBean.getSize()));

            return v;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

    }

    static  class  ViewHolder{
        TextView iv_videoname;
        TextView iv_videoduration;
        TextView iv_videosize;
    }

    /**
     * 开启一个子线程读取视屏资源
     */
    public void getAllVideo(){
        new Thread(){
            @Override
            public void run() {
                if(PermissionUtils.isGrantExternalRW(VideoListActivity.this,1)){
                    getAllVideoDate();
                }
                }

        }.start();
    }

    private void getAllVideoDate() {
        videoItemBeans=new ArrayList<VideoItemBean>();
        ContentResolver resolver=getContentResolver();
        Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        //Uri uri= MediaStore.Video.Media.INTERNAL_CONTENT_URI;
        String[] projection={
                MediaStore.Video.Media.TITLE,  //视屏标题
                MediaStore.Video.Media.DURATION,//时长
                MediaStore.Video.Media.SIZE,//大小
                MediaStore.Video.Media.DATA//觉对路径
        };

        Cursor cursor = resolver.query(uri,projection,null,null,null);

        while (cursor.moveToNext()){
            //过滤大于3M的视屏
            long size = cursor.getLong(2);
            if(size>3*1024*1024){
                VideoItemBean videoItemBean=new VideoItemBean();
                String  title= cursor.getString(0);
                videoItemBean.setTitle(title);
                String duration =cursor.getString(1);
                videoItemBean.setDuration(duration);
                videoItemBean.setSize(size);
                String data=cursor.getString(3);
                videoItemBean.setData(data);
                videoItemBeans.add(videoItemBean);
            }
        }
        handler.sendEmptyMessage(0);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Login(loginId, loginPsd);
                    getAllVideoDate();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"您的手机暂不适配哦~",0).show();
                        }
                    });
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
