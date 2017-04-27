package com.mark.testtx.live.logic;

/**
 * 直播管理
 * Created by Mark.Han on 2017/4/26.
 */
public class TCPlayerMgr {

    public PlayerListener mPlayerListener;


    private TCPlayerMgr() {
    }

    private static class TCPlayerMgrHolder {
        private static TCPlayerMgr instance = new TCPlayerMgr();
    }

    public static TCPlayerMgr getInstance() {
        return TCPlayerMgrHolder.instance;
    }

    public void setPlayerListener(PlayerListener playerListener) {
        mPlayerListener = playerListener;
    }

    /**
     * request回调
     */
    public interface PlayerListener {
        void onRequestCallback(int errCode);
    }

}
