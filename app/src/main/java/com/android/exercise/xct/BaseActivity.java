package com.android.exercise.xct;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {
    private Button btn_left;
    private Button btn_right;
    private TextView tv_title;
    private LinearLayout ll_Content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在代码中设置无标题栏
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        initView();
    }

    private  void  initView(){
        btn_left =  (Button) findViewById(R.id.btn_left);
        btn_right=(Button)findViewById(R.id.btn_right);
        tv_title=(TextView)findViewById(R.id.tv_titile);
        btn_left.setOnClickListener(buttonOnClickLister);
        btn_right.setOnClickListener(buttonOnClickLister);

        ll_Content=findViewById(R.id.ll_child_content);
        View view=setContentView();
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll_Content.addView(view,layoutParams);

    }

    /**
     * 让子类去实现子类的主要布局
     * @return
     */
    public abstract View setContentView();

    /**
     * 设置坐边按钮按钮可见
     * @param visibility
     */
    public void setLeftvisibility(int visibility){
        btn_left.setVisibility(visibility);
    }

    /**
     * 设置右边按钮是否可见
     * @param visibility
     */
    public void setRightvisibility(int visibility){
        btn_right.setVisibility(visibility);
    }

    public void setContentTitle(String title){
        tv_title.setText(title);
    }

    /**
     * 设置左右按钮的点击事件，并且让子类去实现
     */
    View.OnClickListener buttonOnClickLister=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_left:
                    leftButtonClick();
                    break;
                case R.id.btn_right:
                    rightButtonClick();
                    break;
            }
        }
    };

    public abstract void leftButtonClick();
    public abstract void rightButtonClick();


}
