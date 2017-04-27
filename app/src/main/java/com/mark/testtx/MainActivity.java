package com.mark.testtx;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TXLivePlayer mLivePlayer;
    private static final String TAG = "--Main--";
    private TXCloudVideoView mPlayerView;

    private int mCurrentRenderRotation;
    private int mCurrentRenderMode;

    private RelativeLayout relativeLayout;
    private RelativeLayout relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        int[] sdkver = TXLivePusher.getSDKVersion();
        if (sdkver != null && sdkver.length >= 3) {
            Log.d("rtmpsdk", "rtmp sdk version is:" + sdkver[0] + "." + sdkver[1] + "." + sdkver[2]);
        }
        initTXLP();

        findViewById(R.id.all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerView.setRenderMode(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
            }
        });

        relativeLayout = (RelativeLayout) findViewById(R.id.full);
        relative = (RelativeLayout) findViewById(R.id.relative);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void play(View view) {
//        mLivePlayer.startPlay(AppData.pullUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV); //推荐FLV
        Intent intent = new Intent(this, PullActivity.class);
        startActivity(intent);
    }

    /**
     * 定制闹钟提醒，五分钟后直播开始
     * @param v
     */
    public void close(View v) {
//        if (mLivePlayer.isPlaying()) {
//            mLivePlayer.stopPlay(true);
//        }

        Intent intent = new Intent(this, MyClockService.class);
        intent.putExtra("test", "您预约的直播将在5分钟后开始，请注意观看");

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 创建 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        // 设置传入的时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
        // 指定一个日期
        Date date = null;
        try {
            date = dateFormat.parse("2017-4-21 17:33:30");
            // 对 calendar 设置为 date 所定的日期
            calendar.setTime(date);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void join(View v) {
        if (mLivePlayer.isPlaying()) {
            mLivePlayer.stopPlay(true);
        }
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    public void zhuan(View v) {

        if (mCurrentRenderRotation == TXLiveConstants.RENDER_ROTATION_PORTRAIT) {
            mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_LANDSCAPE;
        } else if (mCurrentRenderRotation == TXLiveConstants.RENDER_ROTATION_LANDSCAPE) {
            mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
        }

        mLivePlayer.setRenderRotation(mCurrentRenderRotation);

//        if (mLivePlayer == null) {
//            return;
//        }
//
//        if (mCurrentRenderMode == TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN) {
//            mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
//            mCurrentRenderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
//        } else if (mCurrentRenderMode == TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION) {
//            mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
//            mCurrentRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
//        }
        ViewGroup viewGroup = (ViewGroup) mPlayerView.getParent();
        if (viewGroup == null) {
            return;
        }
        viewGroup.removeAllViews();
        relativeLayout.addView(mPlayerView);
        relativeLayout.setVisibility(View.VISIBLE);
        int mHideFlags =
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        relativeLayout.setSystemUiVisibility(mHideFlags);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        String message = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "屏幕设置为：横屏" : "屏幕设置为：竖屏";
        Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //land
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //port
        }
    }

    @Override
    public void onBackPressed() {
        relativeLayout.setVisibility(View.GONE);
        relativeLayout.removeAllViews();

        ViewGroup last = (ViewGroup) mPlayerView.getParent();//找到videoitemview的父类，然后remove
        if (last != null) {
            last.removeAllViews();
        }
        relative.addView(mPlayerView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    private void initPlayer() {
        TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();

        //自动模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(5);
        mLivePlayer.setConfig(mPlayConfig);
    }

    private void initTXLP() {
        //mPlayerView即step1中添加的界面view
        mPlayerView = (TXCloudVideoView) findViewById(R.id.video_view);
        //创建player对象
        mLivePlayer = new TXLivePlayer(this);
        //关键player对象与界面view
        mLivePlayer.setPlayerView(mPlayerView);
//        mPlayerView.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
//        mPlayerView.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);

        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int i, Bundle bundle) {
//                Log.e(TAG, "onPlayEvent: " + i + "bundle" + bundle.toString());
            }

            @Override
            public void onNetStatus(Bundle bundle) {
//                Log.e(TAG, "onNetStatus: " + "bundle" + bundle.toString());
            }
        });
        initPlayer();
    }


}
