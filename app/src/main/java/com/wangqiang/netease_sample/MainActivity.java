package com.wangqiang.netease_sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wangqiang.libs.splashviewgroup.SplashViewSwicher;
import com.wangqiang.netease_sample.service.SoundService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int MSG_SHOW_NEXT = 0x0;//下一张
    private final int MSG_FINISH = 0x1;   //结束，进入主页
    private final int MSG_PAUSE = 0x2;    //暂停
    private final int MSG_CANCEL = 0x3;   //取消
    private final int AD_DELAY = 1000;    //图片切换时间间隔
    private int adIndex = 0;
    private final TimerHandler timerHandler = new TimerHandler();
    private final int[] ads = {
            R.mipmap.biz_ad_new_version1_img1,
            R.mipmap.biz_ad_new_version1_img2,
            R.mipmap.biz_ad_new_version1_img3,
            R.mipmap.biz_ad_new_version1_img4,
            R.mipmap.biz_ad_new_version1_img5,
            R.mipmap.biz_ad_new_version1_img6,
            R.mipmap.biz_ad_new_version1_img7};
    SplashViewSwicher swicher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swicher = (SplashViewSwicher) findViewById(R.id.swicher);
        swicher.setListener(new SplashViewSwicher.AnimoutFinishListener() {
            @Override
            public void onAnimoutFinish() {
                timerHandler.sendEmptyMessageDelayed(MSG_SHOW_NEXT, AD_DELAY);
            }
        });
        timerHandler.sendEmptyMessage(MSG_SHOW_NEXT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SoundService.startMedia(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, SoundService.class));
    }

    public View makeView() {
        ImageView iv = new ImageView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(lp);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        return iv;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                timerHandler.sendEmptyMessage(MSG_FINISH);
                break;
        }
    }

    class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_NEXT:
                    removeMessages(MSG_SHOW_NEXT);
                    int imageResource = ads[adIndex = (adIndex + 1) % ads.length];
                    ImageView iv = (ImageView) makeView();
                    iv.setImageResource(imageResource);
                    swicher.showView(iv);
                    break;
                case MSG_PAUSE:
                    removeMessages(MSG_PAUSE);
                    break;
                case MSG_FINISH:
                    removeMessages(MSG_SHOW_NEXT);
                    removeMessages(MSG_PAUSE);
                    removeMessages(MSG_CANCEL);
                    removeMessages(MSG_FINISH);
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                    break;
                case MSG_CANCEL:
                    removeMessages(MSG_CANCEL);
                    break;
            }
        }//end handleMessage
    }//end TimerHandler
}
