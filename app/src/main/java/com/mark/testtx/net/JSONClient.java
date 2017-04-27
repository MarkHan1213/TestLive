package com.mark.testtx.net;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.mark.testtx.AppData;
import com.mark.testtx.TCApplication;
import com.mark.testtx.database.CacheViewUtil;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JSONClient {
    private static final String TAG = "JSONClient";

    private static JSONClient jsonClient;
    private final ExecutorService m_sendPool;

    /**
     * Constructor
     */
    private JSONClient() {
        m_sendPool = Executors.newCachedThreadPool();
//		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
    }

    public static JSONClient getInstance() {
        if (jsonClient == null)
            jsonClient = new JSONClient();
        return jsonClient;
    }

    public void cancelInstance() {
        if (jsonClient != null) jsonClient = null;
        if (m_sendPool != null && !m_sendPool.isShutdown()) m_sendPool.shutdown();
    }

    /**
     * 取缓存
     */
    private void sendCacheRequest(JSONRequest request, IJSONResponseCallback handler) throws Exception {
        Log.i(TAG, "sendCacheRequest取缓存");
        JSONObject jsonData = null;
        String[] jsonString = CacheViewUtil.getCache(request.getMethod());
        if (jsonString != null && jsonString[0] != null && jsonString[0].length() > 0)
            jsonData = new JSONObject(jsonString[0]);
        else
            jsonData = new JSONObject();
        handler.handleResponse(jsonData);
    }

    /**
     * 请求网络
     */
    private void sendNetWorkRequest(JSONRequest request, IJSONResponseCallback callback) throws Exception {
        Log.i(TAG, "sendNetWorkRequest");
        ByteArrayOutputStream baos = null;
        InputStream is = null;
        String url = null;
        boolean isPost = request.isM_isPost();

        if (request.isM_QQ() || request.isM_SinaWeibo() || request.isM_Wechat()) {
            url = request.isM_QQ() ?
                    "https://graph.qq.com/user/get_user_info" :
                    "https://api.weibo.com/2/users/show.json";

            url = request.isM_Wechat() ? request.getMethod() : url;

            url = url + "?" + setParametersByQQ(request);

            //将URL与参数拼接
            HttpGet getMethod = new HttpGet(url);

            HttpClient httpClient = new DefaultHttpClient();

            try {
                HttpResponse response = httpClient.execute(getMethod);

                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity(), "utf-8");

                    if (responseString != null && responseString.length() > 0) {
                        try {
                            if (responseString.length() > 400)
                                Log.w(TAG, "调用接口成功！返回数据：responseString:" + responseString.substring(0, 400));
                            else if (responseString.length() > 300)
                                Log.w(TAG, "调用接口成功！返回数据：responseString:" + responseString.substring(0, 300));
                            else if (responseString.length() > 220)
                                Log.w(TAG, "调用接口成功！返回数据：responseString:" + responseString.substring(0, 220));
                            else
                                Log.w(TAG, "调用接口成功！返回数据：responseString:" + responseString);

                            responseString = responseString.trim();

                            if (responseString.indexOf("{") < 0) // 返回失败
                            {
                                if (callback != null) {
                                    callback.handleError(-1);
                                }
                            } else {
                                JSONObject jsonResponse = new JSONObject(responseString);// (Html.fromHtml(ret).toString());

                                if (callback != null) {
                                    callback.handleResponse(jsonResponse);
                                }
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "sendNetWorkRequest bottom JSONException error! " + e.getMessage());

                            if (callback != null) {
                                callback.handleError(-1);
                            }

                            return;
                        } catch (Exception e) {
                            Log.e(TAG, "sendNetWorkRequest bottom error! " + e.getMessage());
                        }
                    } else {
                        Log.i(TAG, "qq or sina response.getEntity() onFailed()");

                        if (callback != null) {
                            callback.handleError(-1);
                        }

                        return;
                    }
                } else {
                    Log.i(TAG, "qq or sina sendNetWorkRequest onError()");

                    if (callback != null) {
                        callback.handleError(0);
                    }

                    return;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (request.isM_New()) {
                    url = AppData.SERVER_NEW_URL + request.getMethod();
                } else if (request.isM_Cas()) {
                    url = AppData.V3_SERVER_URL + request.getMethod();
                } else {
                    url = AppData.SERVER_URL + request.getMethod();
                }

                if (!isPost) {
                    url = url + setParameters(request);
                }

                Log.i(TAG, "url = " + url);

                URL url2 = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
                conn.setConnectTimeout(20 * 1000);
//				conn.setReadTimeout(40 * 1000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(!isPost);
                conn.setInstanceFollowRedirects(true);
                if (isPost)
                    conn.setRequestMethod("POST");
                else
                    conn.setRequestMethod("GET");
                conn.setRequestProperty("Charset", "UTF-8");
//				conn.setRequestProperty("User-agent","Mozilla/4.0");
//				conn.setRequestProperty("Connection", "Keep-Alive");
                if (!isWifiEnable(TCApplication.getApplication()) && conn != null) {
                    conn = setHttpProxy(conn);
                }
//				setBiaduConnection(true);

                if (isPost) {
                    String params = setParameters(request);
                    params = params.replace("?", "&");
                    conn.connect();
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(params);
                    out.flush();
                    out.close();
                }

                Log.d(TAG, "waiting for connection response!");

                // 返回成功
                if (conn != null && conn.getResponseCode() == HttpStatus.SC_OK) {
                    Log.d(TAG, "connection response is ok!");

                    byte[] data = new byte[1024];
                    int count = 0;
                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();

                    while ((count = is.read(data)) > 0) {
                        baos.write(data, 0, count);
                    }
                }
                // 返回失败
                else {
                    // call onError() with the return code. Kari130709
                    Log.i(TAG, "sendNetWorkRequest onError()");

                    if (callback != null) {
                        callback.handleError(0);
                    }

                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
//				setBiaduConnection(false);
                if (!(e instanceof InterruptedException)) {
                    if (callback != null) {
                        Log.e(TAG, "网络连接错误！错误代码 -1! " + e.getMessage());
                        callback.handleError(-1);
                    }
                    return;
                }
            } finally {
                if (baos != null)
                    baos.close();

                if (is != null)
                    is.close();
            }

            if (baos == null) {
                Log.i("json client", "返回失败 baos == null");
                if (callback != null)
                    callback.handleError(-1);
                return;
            }

            try {
                String responseString = baos.toString();
                if (responseString.length() > 400)
                    Log.w(TAG, "调用接口成功！返回数据：responseString:" + responseString.substring(0, 400));
                else if (responseString.length() > 300)
                    Log.w(TAG, "调用接口成功！返回数据：responseString:" + responseString.substring(0, 300));
                else if (responseString.length() > 220)
                    Log.w(TAG, "调用接口成功！返回数据：responseString:" + responseString.substring(0, 220));
                else
                    Log.w(TAG, "调用接口成功！返回数据：responseString:" + responseString);
                responseString = responseString.trim();

                if (responseString.indexOf("{") < 0) // 返回失败
                {
                    if (callback != null)
                        callback.handleError(-1);
                } else {
                    JSONObject jsonResponse = new JSONObject(responseString);// (Html.fromHtml(ret).toString());
                    if (callback != null)
                        callback.handleResponse(jsonResponse);
                }
            } catch (JSONException e) {
                Log.e(TAG, "sendNetWorkRequest bottom JSONException error! " + e.getMessage());
                if (callback != null)
                    callback.handleError(-1);
                return;
            } catch (Exception e) {
                Log.e(TAG, "sendNetWorkRequest bottom error! " + e.getMessage());
            }
        }
    }

    public void setBiaduConnection(final boolean isFirst) {
        try {
//			URL urlBaidu = new URL("http://www.baidu.com");
//			HttpURLConnection conn = (HttpURLConnection)urlBaidu.openConnection();
//			conn.setConnectTimeout(20 * 1000);
//			conn.setRequestMethod("GET");
//			conn.setDoInput(true);
//			conn.setDoOutput(true);
//			conn.setUseCaches(true);
//			conn.setInstanceFollowRedirects(true);
//			if (conn != null && conn.getResponseCode() == HttpStatus.SC_OK) 
//			{
//				if(isFirst)
//					Log.e(TAG, "this is first ! HttpURLConnection  url = http://www.baidu.com  Connection Success!");
//				else
//					Log.e(TAG, "this is catch ! HttpURLConnection  url = http://www.baidu.com  Connection Success!");
//			}
        } catch (Exception e2) {
            if (isFirst)
                Log.e(TAG, "this is first ! HttpURLConnection  url = http://www.baidu.com  Connection failed! this has a exception!");
            else
                Log.e(TAG, "this is catch ! HttpURLConnection  url = http://www.baidu.com  Connection failed! this has a exception!");
            e2.printStackTrace();
        }
    }

    /**
     * 录音上传
     */
    private void uploadRequest(JSONRequest request, IJSONResponseCallback callback) throws Exception {
        Log.i(TAG, "uploadRequest");
        String CONTENT_TYPE = "application/octet-stream";
        ByteArrayOutputStream baos = null;
        InputStream is = null;
        String urlStr = null;

        try {
            if (request.isM_New()) {
                urlStr = AppData.SERVER_NEW_URL + request.getMethod();
            } else if (request.isM_Cas()) {
                urlStr = AppData.V3_SERVER_URL + request.getMethod();
            } else {
                urlStr = AppData.SERVER_URL + request.getMethod();
            }

            urlStr = urlStr + setParameters(request);

            URL url = new URL(urlStr);
            Log.i(TAG, "url = " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(20 * 1000);
            conn.setReadTimeout(40 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-agent", "Mozilla/4.0");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE);

            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            InputStream input = request.getInputStream();

            int size = 1024;
            byte[] buffer = new byte[size];
            int length = -1;
            while ((length = input.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (outStream != null) {
                try {
                    outStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                int retcode = conn.getResponseCode();
                if (retcode == 200) {
                    Log.e(TAG, "retcode == 200!!");
                    byte[] data = new byte[1024];
                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();

                    int count = 0;
                    while ((count = is.read(data)) > 0) {
                        baos.write(data, 0, count);
                    }
                }
            } catch (Exception e) {
                //写的时候没有异常  读的时候发生异常，此处认为上传成功
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("rsp", 0);
                jsonResponse.put("msg", "success");
                callback.handleResponse(jsonResponse);
                return;
            }

            outStream.close();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
            callback.handleError(-1);
        } finally {
            if (baos != null) {
                Log.e(TAG, "baos != null");
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (baos == null) {
            Log.e(TAG, "baos == null");
            callback.handleError(-1);
            return;
        }

        try {
            String responseString = baos.toString();
            responseString = responseString.trim();

            if (responseString.indexOf("{") < 0)
                callback.handleError(-1);
            else {
                Log.i(TAG, "!!!!!!!!!    baos != null  =  " + baos.toString());
                String ret = responseString.substring(responseString.indexOf("{"));
                JSONObject jsonResponse = new JSONObject(ret);
                callback.handleResponse(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String setParametersByQQ(JSONRequest request) {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

        for (String key : request.getM_parameters().keySet()) {
            params.add(new BasicNameValuePair(key, (String) request.getM_parameters().get(key)));
        }

        return URLEncodedUtils.format(params, "UTF-8");
    }

    private String setParameters(JSONRequest request) {
        String string = "?";
        try {
            if (request.getM_parameters().size() > 0) {
                for (String key : request.getM_parameters().keySet()) {
                    Log.i(TAG, "key = " + key + "  value = " + (String) request.getM_parameters().get(key));
                    String value = URLEncoder.encode((String) request.getM_parameters().get(key), "UTF-8");
                    string = string + key + "=" + value + "&";
                }
            }

            if (string.lastIndexOf("&") != -1)
                string = string.substring(0, string.lastIndexOf("&"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return string.trim().replace(" ", "%20");
    }

    // support wap connection
    private boolean isWifiEnable(Context ctx) {
        boolean ret = true;
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null || !wifiManager.isWifiEnabled())
            ret = false;
        return ret;
    }

    /**
     * 设置代理
     */
    private HttpURLConnection setHttpProxy(HttpURLConnection conn) {
        Log.i(TAG, "setHttpProxy android wifi is enable");

        Cursor mCursor = null;

        try {
            Uri uri = Uri.parse("content://telephony/carriers/preferapn"); // get current APN access point
            mCursor = TCApplication.getApplication().getContentResolver().query(uri, null, null, null, null);

            if (mCursor != null && mCursor.moveToFirst()) // move the cursor to the first record ,only one of course
            {
                String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
                String portStr = mCursor.getString(mCursor.getColumnIndex("port"));

                if (proxyStr != null && proxyStr.trim().length() > 0) {
                    conn.setRequestProperty(ConnRouteParams.DEFAULT_PROXY, portStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
        }

        return conn;
    }

    public void sendRequest(JSONRequest request, IJSONResponseCallback callback) throws InterruptedException {
        m_sendPool.submit(new sendRunnable(request, callback));
    }

    /**
     * 发送请求
     */
    private class sendRunnable implements Runnable {
        JSONRequest request = null;
        IJSONResponseCallback callback = null;

        public sendRunnable(JSONRequest request, IJSONResponseCallback callback) {
            this.request = request;
            this.callback = callback;
        }

        public void run() {
            try {
                if (request != null) {
                    // 如果要取缓存，则去取缓存内容,随后刷新，并通知界面加载数据，如果没有缓存，返回可以解析的空的JSONObject。
                    if (request.isM_isCache())
                        sendCacheRequest(request, callback);
                    else if (request.isM_isUpload() && request.getInputStream() != null)
                        uploadRequest(request, callback);
                    else
                        sendNetWorkRequest(request, callback);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "sendRunnable run() error!");
            }
        }
    }
}