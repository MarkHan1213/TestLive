package com.mark.testtx.live.acitivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mark.testtx.R;
import com.mark.testtx.live.customviews.TCInputTextMsgDialog;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class PlayActivity extends AppCompatActivity implements TCInputTextMsgDialog.OnTextSendListener, ITXLivePlayListener {

    private TXLivePlayer mLivePlayer;//播放控制器
    private TXLivePlayConfig mLivePlayerConfig;//播放设置
    private TXCloudVideoView mLivePlayerView;//播放视图

    private LinearLayout mPlayLayout;//播放控制栏
    private LinearLayout mNoticeLayout;//预告栏

    private ImageView mPlayIcon;//播放按钮
    private ImageView mFullIcon;//全屏按钮

    private TCInputTextMsgDialog mInputTextMsgDialog;//输入框

    private boolean mIsLivePlay = true;//是否是直播
    protected boolean mPausing = false;//暂停
    private boolean mPlaying = false;//播放
    /*--------------------点播相关---------------------------------------------------------------*/
    private long mTrackingTouchTS;//调整进度条时的时间
    private SeekBar mSeekBar;//点播时的进度条
    private TextView mTextStart;//已播放时长
    private TextView mTextDuration;//全部时长
    private boolean mStartSeek = false;//是否拖动
    private boolean mVideoPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置窗口属性全屏，没有title等操作
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play);

        initView();
    }

    /**
     * 根据回放或者直播进行初始化界面操作
     */
    private void initView() {
        if (mIsLivePlay) {
            initLiveView();
        } else {
            initVodView();
        }
    }

    /**
     * 初始化观看直播界面
     */
    private void initLiveView() {
        mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);
    }

    /**
     * 初始化观看点播界面
     */
    private void initVodView() {

        mTextStart = (TextView) findViewById(R.id.start_time);
        mTextDuration = (TextView) findViewById(R.id.end_time);
        mPlayIcon = (ImageView) findViewById(R.id.play_btn);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        //设置拖动监听，与播放进度关联
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean bFromUser) {
                if (mTextStart != null) {
                    mTextStart.setText(String.format(Locale.CHINA, "%02d:%02d", (progress % 3600) / 60, (progress % 3600) % 60));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mStartSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mLivePlayer.seek(seekBar.getProgress());
                mTrackingTouchTS = System.currentTimeMillis();
                mStartSeek = false;
            }
        });
    }

    /**
     * 开始播放
     */
    private void startPlay() {

    }

    /**
     * 结束播放
     *
     * @param clearLastFrame 是否清除最后一帧图片
     */
    private void stopPlay(boolean clearLastFrame) {
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(clearLastFrame);
            mPlaying = false;
        }
    }


    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    /**
     * 当聊天发送时
     *
     * @param msg
     * @param tanmuOpen
     */
    @Override
    public void onTextSend(String msg, boolean tanmuOpen) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

//        //消息回显
//        TCChatEntity entity = new TCChatEntity();
//        entity.setSenderName("我:");
//        entity.setContext(msg);
//        entity.setType(TCConstants.TEXT_TYPE);
//        notifyMsg(entity);
//
//        if (danmuOpen) {
//            if (mDanmuMgr != null) {
//                mDanmuMgr.addDanmu(mHeadPic, mNickname, msg);
//            }
//            mTCChatRoomMgr.sendDanmuMessage(msg);
//        } else {
//            mTCChatRoomMgr.sendTextMessage(msg);
//        }
    }

    /*------------------------------播放状态监听--------------------------------------------------*/
    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {//开始播放时，停停止显示加载动画
            // TODO: 2017/4/26 半透明加载动画
//            stopLoadingAnimation();
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) { // 如下这段代码是处理播放进度
            int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS); //进度（秒数）
            int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION); //时间（秒数）

            long curTS = System.currentTimeMillis();
            // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
            if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                return;
            }
            mTrackingTouchTS = curTS;

            // UI进度进行相应的调整
            if (mSeekBar != null) {
                mSeekBar.setProgress(progress);
                mSeekBar.setMax(duration);
            }

            mTextStart.setText(String.format(Locale.CHINA, "%02d:%02d", progress / 60, progress % 60));
            mTextDuration.setText(String.format(Locale.CHINA, "%02d:%02d", duration / 60, duration % 60));

            return;
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {// 如下这段代码是
            mLivePlayerView.onPause();
            stopPlay(true);
            mPausing = true;
            mPlaying = false;
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {//处理播放结束的事件
            stopPlay(false);
            mVideoPause = false;
            if (mTextStart != null) {
                mTextStart.setText(String.format(Locale.CHINA, "%s", "00:00"));
            }
            if (mSeekBar != null) {
                mSeekBar.setProgress(0);
            }
            if (mPlayIcon != null) {
                mPlayIcon.setBackgroundResource(R.drawable.play_start);
            }
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }
}
