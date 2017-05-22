package com.cairongcai.rocket;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class RocketService extends Service {

    private WindowManager wm;
    private WindowManager.LayoutParams mParams=new WindowManager.LayoutParams();
    private int screenheight;
    private int screenwidth;
    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            mParams.y=(Integer) msg.obj;
            wm.updateViewLayout(rocket,mParams);
        }
    };
    private View rocket;

    public RocketService() {
    }

    @Override
    public void onCreate() {
        //初始化窗体对象
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        screenheight = wm.getDefaultDisplay().getHeight();
        screenwidth = wm.getDefaultDisplay().getWidth();
        
        showRocket();

        super.onCreate();
    }

    private void showRocket() {
        rocket = View.inflate(this, R.layout.rocket_view,null);
        mParams.width=WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height=WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.type=WindowManager.LayoutParams.TYPE_PHONE;
        mParams.gravity= Gravity.TOP|Gravity.LEFT;
        ImageView iv_rocket= (ImageView) rocket.findViewById(R.id.iv_rocket);
        AnimationDrawable drawable= (AnimationDrawable) iv_rocket.getBackground();
        drawable.start();
        wm.addView(rocket,mParams);
        iv_rocket.setOnTouchListener(new View.OnTouchListener() {

            private int startY;
            private int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX= (int) event.getRawX();
                        int moveY= (int) event.getRawY();

                        int disX=moveX- startX;
                        int disY=moveY- startY;

                        mParams.x=mParams.x+disX;
                        mParams.y=mParams.y+disY;

                        if(mParams.x<0)
                        {
                            mParams.x=0;
                        }
                        if(mParams.y<0)
                        {
                            mParams.y=0;
                        }
                        if(mParams.x>screenwidth- rocket.getWidth())
                        {
                            mParams.x=screenwidth- rocket.getWidth();
                        }
                        if(mParams.y>screenheight-22- rocket.getHeight()){
                           mParams.y=screenheight-22- rocket.getHeight();
                        }
                        wm.updateViewLayout(rocket,mParams);
                        startX= (int) event.getRawX();
                        startY=(int)event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if( mParams.y>900)
                        {
                            sendRocket();
                            Intent intent=new Intent(getApplicationContext(),BackgroundActivity.class);
                            //从服务中开启activity，需要设置一个新的activity栈
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        }

                }
                //只响应触摸事件，返回true
                return true;
            }
        });
    }

    private void sendRocket() {
        new Thread()
        {
            @Override
            public void run() {
                for(int i=0;i<11;i++)
                {
                    int y=350-35*i;
                    SystemClock.sleep(20);
                    Message msg=Message.obtain();
                    msg.obj=y;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        if(wm!=null&&rocket!=null)
        {
            wm.removeView(rocket);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
