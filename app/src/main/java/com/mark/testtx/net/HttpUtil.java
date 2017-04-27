package com.mark.testtx.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.mark.testtx.ResponseData;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;

public class HttpUtil {
    private final static boolean isLog = true;
    public static AsyncHttpClient client = new AsyncHttpClient();    //实例化对象

    static {
        client.setTimeout(30000);   //设置链接超时，如果不设置，默认为10s
    }

    public static void get(String urlString, AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象
    {
        if (client == null) {
            client = new AsyncHttpClient();
        }

        if (isLog) {
            Log.i("HttpUtil", "" + urlString + "?");
        }
        client.get(urlString, res);
    }

    public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参数
    {
        if (client == null) {
            client = new AsyncHttpClient();
        }

        if (isLog) {
            Log.i("HttpUtil", "带参数 " + urlString + "?" + params.toString());
        }
        client.get(urlString, params, res);
    }

    public static void getZfb(String urlString, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参数
    {
        SyncHttpClient c = new SyncHttpClient();
        if (isLog) {
            Log.i("HttpUtil", "带参数 " + urlString + "?" + params.toString());
        }
        c.get(urlString, params, res);
    }

    public static void get(String urlString, JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
        if (isLog) {
            Log.i("HttpUtil", "get111" + urlString);
        }
        client.get(urlString, res);
    }

    public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
        if (isLog) {
            Log.i("HttpUtil", "get2222" + urlString + "?" + params.toString());
        }
        client.get(urlString, params, res);
    }

    public static void get(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {

        client.get(uString, bHandler);
    }

    public static void post(String url, RequestParams params, ResponseHandlerInterface handler) {
        client.post(url, params, handler);
    }

    public static void post(Context context, String url, HttpEntity httpEntity, ResponseHandlerInterface handler) {
        client.post(context, url, httpEntity, RequestParams.APPLICATION_JSON, handler);
    }

    public static void post(Context context, String url, JSONObject jsonObject, ResponseHandlerInterface handler) {
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post(context, url, entity, handler);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

    public static void cancelClient() {
        client = null;
    }

    public static void get(String urlString, RequestParams request,
                           final Class<?> classOfT, final int requestCode,
                           final RequestCallbackData callback) {

        if (client == null) {
            client = new AsyncHttpClient();
        }
        final ResponseData mResponseData = new ResponseData();
        // Log.i(TAG, "urlString:" + urlString + "request" +
        // request.toString());
        client.get(urlString, request, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub

                // Log.i(TAG, "logout onFailure!");

                // String jsonString = new String(responseBody);
                // try {
                // Gson gson = new Gson();
                // Object fromJson = gson.fromJson(jsonString, classOfT);
                // mResponseData.obj = fromJson;
                // mResponseData.requestCode = requestCode;
                // callback.showData(mResponseData);
                // } catch (Exception e) {
                // e.printStackTrace();
                // }
                if (mResponseData != null && callback != null) {
                    mResponseData.errCode = -1;
                    callback.showData(mResponseData);
                }

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
                // TODO Auto-generated method stub

                // Log.i(TAG, "logout onSuccess!");

                if (mResponseData != null) {
                    // Gson gson = null;
                    try {
                        String jsonString = new String(responseBody);
                        mResponseData.jsonString = jsonString;
                        Gson gson = new Gson();

                        Object fromJson = gson.fromJson(jsonString, classOfT);
                        mResponseData.obj = fromJson;
                        mResponseData.requestCode = requestCode;
                        if (fromJson != null) {
                            if (NetBean.class.isInstance(fromJson)) {
                                NetBean nb = (NetBean) fromJson;
                                mResponseData.ret = nb.ret;
                                mResponseData.msg = nb.msg;
                            }
                        }
                        if (callback != null) {
                            callback.showData(mResponseData);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mResponseData.errCode = -2;
                        if (callback != null) {
                            callback.showData(mResponseData);
                        }
                    }
                }

            }

        });
    }
}
