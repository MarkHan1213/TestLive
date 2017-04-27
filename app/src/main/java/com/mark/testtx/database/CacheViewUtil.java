package com.mark.testtx.database;

import android.util.Log;

import org.json.JSONObject;

import java.util.Date;

/**
 * @TODO ：view json缓存工具类
 */
public class CacheViewUtil 
{
	private static final String TAG = "CacheViewUtil";
	private static final int TOTAL_RECORD = 500;
	private static final long CACHE_TIME = 1 * 24 * 3600 * 1000 ; //1天（缓存超过TOTAL_RECORD条，删掉一天前的数据）

	//向列表json列表数据库中插入数据
	public static void insertIntoListJsonCache(String key, JSONObject data){
		CacheViewDao jsonDao = CacheViewDao.getInstance();
		jsonDao.insertJsonDate(key, data.toString());
	}
	
	//向列表json列表数据库中删除数据
	public static void deleteListJsonCache(long time){
		CacheViewDao jsonDao = CacheViewDao.getInstance();
		jsonDao.deleteJsonDate(time);
	}
	
	public static void deleteAJsonCacheById(String Id){
		CacheViewDao jsonDao = CacheViewDao.getInstance();
		jsonDao.deleteAJsonDateById(Id);
	}
	
	//向列表json列表数据库中查询数据
	public static String[] queryIntoListJsonCache(String key){
		CacheViewDao jsonDao = CacheViewDao.getInstance();
		return jsonDao.qureyCachejson( key);
	}
	
	//向列表json列表数据库中查询数据总数
	public static int queryIntoListJsonCacheCount(){
		CacheViewDao jsonDao = CacheViewDao.getInstance();
		return jsonDao.qureyCachejsonCount();
	}
	//更新数据
	public static void upDateIntoListJsonCache(String key){
		CacheViewDao jsonDao = CacheViewDao.getInstance();
		jsonDao.upDateJsonDate(key);
	}

	public static boolean isHaveCache(String methodIsCacheKey)
	{
		String[] arrString = null;
		if(methodIsCacheKey != null && methodIsCacheKey.length() > 0)
		{
			//拉取缓存,得到缓存数据
			arrString = CacheViewUtil.queryIntoListJsonCache(methodIsCacheKey);
		}
		
		if(arrString != null && arrString[0] != null && arrString[0].length() > 0)
			return true;
		else
			return false;
	}
	//取缓存
	public static String[] getCache(String cachekey){
		
		if(cachekey != null && cachekey.length() > 0)
		{
			//拉取缓存,得到缓存数据
			long requestTime = System.currentTimeMillis();
			String[] arrString = CacheViewUtil.queryIntoListJsonCache(cachekey);
			Log.i(TAG, String.valueOf(System.currentTimeMillis() - requestTime));
			
			//如果缓存条数大于TOTAL_RECORD,就删除1天前的数据
			int total = 0;
			try
			{
				total = CacheViewUtil.queryIntoListJsonCacheCount();
			}
			catch(Exception e)
			{
				Log.e(TAG, e.getMessage());
			}
			
			Log.i(TAG, "缓存条数:"+total);
			
			if(total > TOTAL_RECORD)
			{
				long deleteBeforeTime =(new Date()).getTime() - CACHE_TIME;
				deleteListJsonCache(deleteBeforeTime);
			}
			
			Log.i(TAG, "get cache, key="+ cachekey);
			
			if (arrString[0]!= null && arrString[0].length() > 0) 
			{
				Log.i(TAG, String.valueOf(System.currentTimeMillis() - requestTime));
				return arrString;
			}
		}
		return null;
	}
	
	//判断是否取缓存,第一页&&不是来自缓存内容的返回true
	public static boolean isgetCache(int offset, boolean isFormCache)
	{
		if(offset == 0 && !isFormCache)
			return true;
		else
			return false;
	}
}
