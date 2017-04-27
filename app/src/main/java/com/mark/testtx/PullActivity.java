package com.mark.testtx;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mark.testtx.live.anim.TCFrequeControl;
import com.mark.testtx.live.anim.TCHeartLayout;
import com.mark.testtx.live.base.TCConstants;
import com.mark.testtx.live.logic.TCChatRoomMgr;
import com.mark.testtx.live.logic.TCPlayerMgr;
import com.mark.testtx.live.logic.TCSimpleUserInfo;
import com.tencent.TIMMessage;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PullActivity extends AppCompatActivity implements ITXLivePlayListener, TCChatRoomMgr.TCChatRoomListener, TCPlayerMgr.PlayerListener {

    private RelativeLayout full_screen, dp300_screen;
    private LinearLayout ll_parent;
    private Button send, change;
    private ListView listView;
    private EditText editText;

    private TXCloudVideoView mPlayerView;

    private TXLivePlayer mLivePlayer;

    private List<String> stringList;
    private ArrayAdapter<String> arrayAdapter;

    //    protected String mPlayUrl = "http://2527.vod.myqcloud.com/2527_000007d04afea41591336f60841b5774dcfd0001.f0.flv";
    protected String mPlayUrl = AppData.pullUrl;


    //点播相关
    private long mTrackingTouchTS = 0;
    private SeekBar seekBar;
    private boolean mStartSeek = false;
    private TextView mTextProgress;

    //点赞动画
    private TCHeartLayout mHeartLayout;
    //点赞频率控制
    private TCFrequeControl mLikeFrequeControl;
    private long mHeartCount = 0;


    private static final String TAG = "--Main--";
    private TCChatRoomMgr mTCChatRoomMgr;

    private int mPlayType = 0;      //点播还是直播，拉列表协议返回的
    private int mUrlPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    private boolean mIsLivePlay;
    private TCPlayerMgr mTCPlayerMgr;

    protected boolean mPausing = false;
    private boolean mPlaying = false;
    private TXLivePlayConfig mPlayConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getIntent().getStringExtra("url");
        initView();
        initVideo();
        initData();
        mTCChatRoomMgr = TCChatRoomMgr.getInstance();

        //在这里停留，让列表界面卡住几百毫秒，给sdk一点预加载的时间，形成秒开的视觉效果
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        stringList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
        listView.setAdapter(arrayAdapter);
    }

    private void initVideo() {

        mPlayerView = (TXCloudVideoView) findViewById(R.id.video_view);

        //创建player对象
        mLivePlayer = new TXLivePlayer(this);
        //关键player对象与界面view
        mLivePlayer.setPlayerView(mPlayerView);
        //设置模式为自适应
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);

        //设置播放监听
        mLivePlayer.setPlayListener(this);
        //播放设置
        mPlayConfig = new TXLivePlayConfig();

        //自动模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(5);
        mLivePlayer.setConfig(mPlayConfig);

//        mLivePlayer.startPlay(AppData.pullUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV); //推荐FLV
        mLivePlayer.startPlay(AppData.pullUrl, TXLivePlayer.PLAY_TYPE_VOD_MP4); //推荐FLV mp4 点播
    }

    private void initView() {

        full_screen = (RelativeLayout) findViewById(R.id.full_screen);
        dp300_screen = (RelativeLayout) findViewById(R.id.dp300_screen);
        dp300_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeartLayout != null) {
                    mHeartLayout.addFavor();
                }

                //点赞发送请求限制
                if (mLikeFrequeControl == null) {
                    mLikeFrequeControl = new TCFrequeControl();
                    mLikeFrequeControl.init(2, 1);
                }
                if (mLikeFrequeControl.canTrigger()) {
                    mHeartCount++;
                    //向后台发送点赞信息
//                    mTCPlayerMgr.addHeartCount(mPusherId);
                    //向ChatRoom发送点赞消息
                    mTCChatRoomMgr.sendPraiseMessage();
                }
            }
        });
        mHeartLayout = (TCHeartLayout) findViewById(R.id.heartLayout);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        send = (Button) findViewById(R.id.send);
        change = (Button) findViewById(R.id.change);
        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.input);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        mTextProgress = (TextView) findViewById(R.id.progress_text);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                stringList.add(message);
                arrayAdapter.notifyDataSetChanged();
                listView.setSelection(stringList.size() - 1);

                editText.setText("");
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mTextProgress != null) {
                    mTextProgress.setText(String.format(Locale.CHINA, "%02d:%02d:%02d/%02d:%02d:%02d", progress / 3600, (progress % 3600) / 60, (progress % 3600) % 60, seekBar.getMax() / 3600, (seekBar.getMax() % 3600) / 60, (seekBar.getMax() % 3600) % 60));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mStartSeek = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 调整进度
                mLivePlayer.seek(seekBar.getProgress());
                mLivePlayer.resume();
                mStartSeek = true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //land
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //port
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String message = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "屏幕设置为：横屏" : "屏幕设置为：竖屏";
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            dp300_screen.setLayoutParams(layoutParams);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) dp300_screen.getLayoutParams();
            layoutParams.height = dip2px(this, 300);
            dp300_screen.setLayoutParams(layoutParams);
        }
    }

    private boolean checkPlayUrl() {
        if (TextUtils.isEmpty(mPlayUrl) || (!mPlayUrl.startsWith("http://") && !mPlayUrl.startsWith("https://") && !mPlayUrl.startsWith("rtmp://"))) {
            Toast.makeText(getApplicationContext(), "播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mIsLivePlay) {
            if (mPlayUrl.startsWith("rtmp://")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
            } else if ((mPlayUrl.startsWith("http://") || mPlayUrl.startsWith("https://")) && mPlayUrl.contains(".flv")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
            } else {
                Toast.makeText(getApplicationContext(), "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (mPlayUrl.startsWith("http://") || mPlayUrl.startsWith("https://")) {
                if (mPlayUrl.contains(".flv")) {
                    mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
                } else if (mPlayUrl.contains(".m3u8")) {
                    mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
                } else if (mPlayUrl.toLowerCase().contains(".mp4")) {
                    mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
                } else {
                    Toast.makeText(getApplicationContext(), "播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getApplicationContext(), "播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        // 暂停
        if (mLivePlayer.isPlaying()) {
            mLivePlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //继续播放
        if (!mLivePlayer.isPlaying()) {
            mLivePlayer.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLivePlayer.stopPlay(true); // true代表清除最后一帧画面
        mPlayerView.onDestroy();
    }


    @Override
    public void onPlayEvent(int i, Bundle bundle) {
        if (i == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            if (mStartSeek) {
                return;
            }
            int progress = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
            int duration = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION);
            long curTS = System.currentTimeMillis();
            // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
            if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                return;
            }
            mTrackingTouchTS = curTS;

            if (seekBar != null) {
                seekBar.setProgress(progress);
            }
            if (mTextProgress != null) {
                mTextProgress.setText(String.format(Locale.CHINA, "%02d:%02d:%02d/%02d:%02d:%02d", progress / 3600, (progress % 3600) / 60, progress % 60, duration / 3600, (duration % 3600) / 60, duration % 60));
            }

            if (seekBar != null) {
                seekBar.setMax(duration);
            }
        } else if (i == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {

//            showErrorAndQuit(TCConstants.ERROR_MSG_NET_DISCONNECTED);
            Toast.makeText(PullActivity.this, "网络错误", Toast.LENGTH_SHORT).show();

        } else if (i == TXLiveConstants.PLAY_EVT_PLAY_END) {
            stopPlay(false);
            if (mTextProgress != null) {
                mTextProgress.setText(String.format(Locale.CHINA, "%s", "00:00:00/00:00:00"));
            }
            if (seekBar != null) {
                seekBar.setProgress(0);
            }

        }

    }

    /**
     * 观众加入房间操作
     */
    public void joinRoom() {

        //初始化消息回调，当前存在：获取文本消息、用户加入/退出消息、群组解散消息、点赞消息、弹幕消息回调
        mTCChatRoomMgr = TCChatRoomMgr.getInstance();
        mTCPlayerMgr = TCPlayerMgr.getInstance();
        mTCPlayerMgr.setPlayerListener(this);

//        if (mIsLivePlay) {
        //仅当直播时才进行执行加入直播间逻辑
        //需要不管如何都可以进直播间聊天室
        mTCChatRoomMgr.setMessageListener(this);
//        mTCChatRoomMgr.joinGroup(mGroupId);
//        }

//        mTCPlayerMgr.enterGroup(mUserId, mPusherId, mIsLivePlay ? mGroupId : mFileId, mNickname, mHeadPic, mIsLivePlay ? 0 : 1);

        startPlay();
    }

    protected void startPlay() {
        if (!checkPlayUrl()) {
            return;
        }

        if (mLivePlayer == null) {
            mLivePlayer = new TXLivePlayer(this);
        }
        if (mPlayerView != null) {
            mPlayerView.clearLog();
        }
        mLivePlayer.setPlayerView(mPlayerView);
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mLivePlayer.setPlayListener(this);

        int result;
        result = mLivePlayer.startPlay(mPlayUrl, mUrlPlayType);

        if (0 != result) {

            Intent rstData = new Intent();

            if (-1 == result) {
                Log.d(TAG, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            } else {
                Log.d(TAG, TCConstants.ERROR_RTMP_PLAY_FAILED);
                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            }

            mPlayerView.onPause();
            stopPlay(true);
//            setResult(TCLiveListFragment.START_LIVE_PLAY, rstData);
            finish();
        } else {
            mPlaying = true;
        }
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(clearLastFrame);
            mPlaying = false;
        }
    }


    @Override
    public void onNetStatus(Bundle bundle) {
        if (mPlayerView != null) {
            mPlayerView.setLogText(bundle, null, 0);
        }

        if (bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
            if (mLivePlayer != null)
                mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
        } else if (mLivePlayer != null)
            mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
    }

    /**
     * dp转为px
     *
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    private int dip2px(Context context, float dipValue) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }


    /*=============================== 聊天室相关方法 开始 =======================================*/
    @Override
    public void onJoinGroupCallback(int code, String msg) {

    }

    @Override
    public void onSendMsgCallback(int code, TIMMessage timMessage) {

    }

    @Override
    public void onReceiveMsg(int type, TCSimpleUserInfo userInfo, String content) {

    }

    @Override
    public void onGroupDelete() {

    }
    /*=============================== 聊天室相关方法结束 ======================================= */


    /**/
    @Override
    public void onRequestCallback(int errCode) {

    }

    /*-------------------------------------来电话时的监听-----------------------------------------*/
    final PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mLivePlayer != null) mLivePlayer.setMute(true);
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mLivePlayer != null) mLivePlayer.setMute(true);
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mLivePlayer != null) mLivePlayer.setMute(false);
                    break;
            }
        }
    };


}
