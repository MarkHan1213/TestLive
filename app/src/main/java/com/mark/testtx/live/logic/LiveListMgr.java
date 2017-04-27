package com.mark.testtx.live.logic;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mark.testtx.AppData;
import com.mark.testtx.live.bean.LiveInfo;
import com.mark.testtx.net.HttpUtil;
import com.tencent.rtmp.TXLog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播列表管理
 * Created by Mark.Han on 2017/4/26.
 */
public class LiveListMgr {
    private static final String TAG = LiveListMgr.class.getSimpleName();

    public static final int LIST_TYPE_LIVE = 1;
    public static final int LIST_TYPE_VOD = 2;
    public static final int LIST_TYPE_ALL = 3;
    public static final int LIST_TYPE_UGC = 5;

    private static final int PAGESIZE = 20;

    private boolean mIsFetching;
    private static Context mContext;

    private ArrayList<LiveInfo> mLiveInfoList = new ArrayList<>();
    private ArrayList<LiveInfo> mVodInfoList = new ArrayList<>();
    private ArrayList<LiveInfo> mUGCInfoList = new ArrayList<>();

    private LiveListMgr() {
        mIsFetching = false;
    }

    private static class LiveListMgrHolder {
        private static LiveListMgr liveListMgr = new LiveListMgr();
    }

    public static LiveListMgr getInstance(Context context) {
        mContext = context;
        return LiveListMgrHolder.liveListMgr;
    }


    /**
     * 分页获取完整列表
     *
     * @param type     类型。1：直播 2：点播 3： 全部 4： 短视频
     * @param listener 列表回调，每获取到一页数据回调一次
     */
    public boolean reloadLiveList(int type, Listener listener) {
        if (mIsFetching) {
            TXLog.w(TAG, "reloadLiveList ignore when fetching");
            return false;
        }
        TXLog.d(TAG, "fetchLiveList start");
        switch (type) {
            case LIST_TYPE_LIVE:
                mLiveInfoList.clear();
                break;
            case LIST_TYPE_VOD:
                mVodInfoList.clear();
                break;
            case LIST_TYPE_UGC:
                mUGCInfoList.clear();
                break;
        }
        getLiveList(type, 1, PAGESIZE, listener);
        mIsFetching = true;
        return true;
    }

    /**
     * 获取列表
     *
     * @param type     1:拉取在线直播列表 2:拉取7天内录播列表 3:拉取在线直播和7天内录播列表，直播列表在前，录播列表在后 4:UGC
     * @param pageNo   页数
     * @param pageSize 每页个数
     */
    private void getLiveList(final int type, final int pageNo, final int pageSize, final Listener listener) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("flag", type);
//        jsonObject.addProperty("pageno", pageNo);
//        jsonObject.addProperty("pagesize", pageSize);
//        jsonObject.addProperty("Action", "FetchList");
        String url = AppData.BASE_URL;
//        RequestParams requestParams = new RequestParams();
//        requestParams.setContentEncoding(RequestParams.APPLICATION_JSON);
//        requestParams.put("Action", "FetchList");
//        requestParams.put("flag", type);
//        requestParams.put("pageno", pageNo);
//        requestParams.put("pagesize", pageSize);

        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("Action", "FetchList");
        jsonObject.put("flag", type);
        jsonObject.put("pageno", pageNo);
        jsonObject.put("pagesize", pageSize);


        HttpUtil.post(mContext, url, jsonObject, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("returnValue");
                    if (code != 0) {
                        listener.onLiveList(code, null, true);
                        return;
                    }
                    JSONObject jsonObject = response.getJSONObject("returnData");
                    int totalcount = jsonObject.getInt("totalcount");
                    JSONArray record = jsonObject.getJSONArray("pusherlist");

//                    Type listType = new TypeToken<ArrayList<LiveInfo>>() {
//                    }.getType();

                    ArrayList<LiveInfo> dataList;
                    switch (type) {
                        case LIST_TYPE_LIVE:
                            dataList = mLiveInfoList;
                            break;
                        case LIST_TYPE_VOD:
                            dataList = mVodInfoList;
                            break;
                        case LIST_TYPE_UGC:
                            dataList = mUGCInfoList;
                            break;
                        default:
                            dataList = new ArrayList<>();
                            break;
                    }
                    if (record != null) {
//                        ArrayList<LiveInfo> result = new Gson().fromJson(record.toString(), listType);
                        List<LiveInfo> result = (List<LiveInfo>) JSON.parse(record.toString());
                        if (result != null && result.isEmpty()) {
                            dataList.addAll(result);
                            if (dataList.size() >= totalcount || pageNo * PAGESIZE >= totalcount) {
                                mIsFetching = false;
                            } else {
                                getLiveList(type, pageNo + 1, PAGESIZE, listener);
                            }
                        } else {
                            mIsFetching = false;
                        }
                        if (listener != null) {
                            listener.onLiveList(code, dataList, pageNo == 1);
                        }
                    } else {
                        listener.onLiveList(code, null, true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (listener != null) {
                    try {
                        int code = errorResponse.getInt("returnValue");
                        listener.onLiveList(code, null, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mIsFetching = false;
            }
        });

    }

    /**
     * 视频列表获取结果回调
     */
    public interface Listener {
        /**
         * @param retCode 获取结果，0表示成功
         * @param result  列表数据
         * @param refresh 是否需要刷新界面，首页需要刷新
         */
        void onLiveList(int retCode, final ArrayList<LiveInfo> result, boolean refresh);
    }

}
