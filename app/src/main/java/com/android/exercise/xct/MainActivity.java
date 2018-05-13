package com.android.exercise.xct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.exercise.xct.video;

public class MainActivity extends BaseActivity {

    private GridView gridView;

    private int[] ids={R.mipmap.phone_category_movie_selected,R.mipmap.phone_category_music_selected,
    R.mipmap.phone_category_tv_selected,R.mipmap.phone_category_hot_selected,R.mipmap.phone_category_life_selected,
    R.mipmap.phone_category_variety_selected};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLeftvisibility(View.GONE);
        setContentTitle("新闻标题");

        gridView=(GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new MyAdaper());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent intent=new Intent(MainActivity.this,videoListActivity.class);
                        Toast.makeText(MainActivity.this,"点击了第一个",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this,"点击了其他",Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });

    }



    private class  MyAdaper extends BaseAdapter{
        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
          View v;
          ViewHolder holder;
          if(view!=null){
              v=view;
             holder= (ViewHolder) v.getTag();
          }else{
              v=View.inflate(MainActivity.this,R.layout.main_item,null);
              holder=new ViewHolder();
              holder.iv_icon=v.findViewById(R.id.iv_itme);
              v.setTag(holder);
          }
            /**
             * 設置每一張圖片
             */
          holder.iv_icon.setImageResource(ids[i]);

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

    static class  ViewHolder{
        ImageView iv_icon;
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
