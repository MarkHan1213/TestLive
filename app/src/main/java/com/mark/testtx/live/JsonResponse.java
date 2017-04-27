package com.mark.testtx.live;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * 腾讯云json回调
 * Created by Mark.Han on 2017/4/26.
 */
public class JsonResponse extends JsonHttpResponseHandler {

    private JsonCallBack jsonCallBack;

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

        if (statusCode != 200) {
            jsonCallBack.onFailure(statusCode, "发生错误");
            return;
        }


        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.toString());
        int code = jsonObject.getInteger("returnValue");
        if (code == 0) {
            jsonCallBack.onSuccess((com.alibaba.fastjson.JSONObject) jsonObject.get("returnData"));
        } else {
            String msg = jsonObject.getString("returnMsg");
            jsonCallBack.onFailure(code, msg);
        }

    }

    public interface JsonCallBack {
        void onSuccess(com.alibaba.fastjson.JSONObject jsonObject);

        void onFailure(int code, String msg);
    }

}
