package com.mark.testtx.net;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mark.testtx.ResponseData;

import org.apache.http.Header;


public class HttpRequestUtil {
	public String TAG = "HttpRequestUtil";

	public HttpRequestUtil() {
		// TODO Auto-generated constructor stub

		newGet();

	}

	public void newGet() // url里面带参数
	{
		if (HttpUtil.client == null) {
			HttpUtil.client = new AsyncHttpClient();

		}
	}

	public ResponseData mResponseData = null;

	public void get(String urlString, RequestParams request,
					final Class<?> classOfT, final int requestCode,
					final RequestCallbackData callback) {
		mResponseData = new ResponseData();
		Log.i(TAG, "urlString:" + urlString + "request" + request.toString());
		HttpUtil.client.get(urlString, request, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				Log.i(TAG, "logout onSuccess!");

				if (mResponseData != null) {
					// Gson gson = null;
					try {
						String jsonString = new String(responseBody);
						mResponseData.jsonString = jsonString;
						Gson gson = new Gson();
						Object fromJson = gson.fromJson(jsonString, classOfT);
						mResponseData.obj = fromJson;
						mResponseData.requestCode = requestCode;
						if (callback != null) {
							callback.showData(mResponseData);
						}
					} catch (Exception e) {
						// e.printStackTrace();
						mResponseData.errCode = -2;
						if (callback != null) {
							callback.showData(mResponseData);
						}
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Log.i(TAG, "logout onFailure!");

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
				if (mResponseData != null) {
					mResponseData.errCode = -1;
					callback.showData(mResponseData);
				}
			}

		});
	}
}
