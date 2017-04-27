
package com.mark.testtx;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.widget.Button;

import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

public class JoinActivity extends AppCompatActivity {

    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;
    private TXCloudVideoView mCaptureView;

    private Bitmap mBitmap;

    private boolean ischange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jonin);
        mCaptureView = (TXCloudVideoView) findViewById(R.id.video_view);
        initLive();
    }

    public void change(View view) {
        if (ischange) {
            mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
            ((Button) view).setText("竖屏");
            ischange = false;
        } else {
            mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
            ((Button) view).setText("横屏");
            ischange = true;
        }
        mLivePusher.setConfig(mLivePushConfig);
    }

    private void initLive() {
        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();

        mBitmap = decodeResource(getResources(), R.drawable.watermark);

        mLivePusher.startPusher(AppData.pushUrl);
        mLivePushConfig.setFrontCamera(false);
        mLivePusher.setBeautyFilter(7, 3);
        mLivePushConfig.setTouchFocus(false);
        //设置视频水印
        mLivePushConfig.setWatermark(BitmapFactory.decodeResource(getResources(), R.drawable.watermark), 10, 10);
        mLivePushConfig.setPauseImg(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
        mLivePusher.setConfig(mLivePushConfig);
        play();
    }

    private void play() {
        mLivePusher.startCameraPreview(mCaptureView);
    }

    // activity 的 onStop 生命周期函数
    @Override
    public void onStop() {
        super.onStop();
        mCaptureView.onPause();  // mCaptureView 是摄像头的图像渲染view
        mLivePusher.pausePusher(); // 通知 SDK 进入“后台推流模式”了
    }

    // activity 的 onStop 生命周期函数
    @Override
    public void onResume() {
        super.onResume();
        mCaptureView.onResume();     // mCaptureView 是摄像头的图像渲染view
        mLivePusher.resumePusher();  // 通知 SDK 重回前台推流
    }

    //结束推流，注意做好清理工作
    public void stopRtmpPublish() {
        mLivePusher.stopCameraPreview(true); //停止摄像头预览
        mLivePusher.stopPusher();            //停止推流
        mLivePusher.setPushListener(null);   //解绑 listener
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRtmpPublish();
    }

    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 自动旋转打开，Activity随手机方向旋转之后，只需要改变推流方向
        int mobileRotation = getWindowManager().getDefaultDisplay().getRotation();
        int pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_90:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                break;
            case Surface.ROTATION_270:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                break;
            default:
                break;
        }

        //通过设置config是设置生效（可以不用重新推流，腾讯云是少数支持直播中热切换分辨率的云商之一）
        mLivePusher.setRenderRotation(0);
        mLivePushConfig.setHomeOrientation(pushRotation);
        mLivePusher.setConfig(mLivePushConfig);
    }

}
