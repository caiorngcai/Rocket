package com.cairongcai.rocket;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class BackgroundActivity extends Activity {
    private Handler mHandeler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        //给图片设置动画
        ImageView iv_button= (ImageView) findViewById(R.id.iv_button);
        ImageView iv_top= (ImageView) findViewById(R.id.iv_top);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(500);
        iv_button.setAnimation(alphaAnimation);
        iv_top.setAnimation(alphaAnimation);

        mHandeler.sendEmptyMessageDelayed(0,1000);
    }
}
