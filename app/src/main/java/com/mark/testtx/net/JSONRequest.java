package com.mark.testtx.net;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JSONRequest {
    //Method to be called
    private String m_method;

    private Map<String, Object> m_parameters;

    private boolean m_isCache = false; //是否取缓存
    private boolean m_isUpload = false; //是否是上传
    private boolean m_isPost = false;//是否用Post上传
    private boolean m_isNew = false;  //是否是base新接口
    private boolean m_isCas = false;  //是否是cas接口
    private boolean m_isQQ = false;
    private boolean m_isSinaWeibo = false;
    private boolean m_isWechat = false;
    private InputStream m_inputStream = null; //文件流

    /**
     * Constructor
     *
     * @param method    The name of the method to invoke.
     * @param m_isCache if get the cache
     */
    public JSONRequest(String method, boolean isCache, String access_token) {
        m_method = method;
        m_parameters = new HashMap<String, Object>();
        if (access_token != null) addParameter("access_token", access_token);
        m_isCache = isCache;
    }

    public void setIsUpload(boolean isUpload) {
        this.m_isUpload = isUpload;
    }

    public void setInputStream(InputStream inputStream) {
        this.m_inputStream = inputStream;
    }

    public void setIsPost(boolean isPost) {
        this.m_isPost = isPost;
    }

    public void setIsNew(boolean isNew) {
        this.m_isNew = isNew;
    }

    public void setIsCas(boolean isCas) {
        this.m_isCas = isCas;
    }

    public void setIsQQ(boolean isQQ) {
        this.m_isQQ = isQQ;
    }

    public void setIsSinaWeibo(boolean isSina) {
        this.m_isSinaWeibo = isSina;
    }

    public void setIsWechat(boolean isWechat) {
        this.m_isWechat = isWechat;
    }

    public boolean isM_isCache() {
        return m_isCache;
    }

    public boolean isM_isUpload() {
        return m_isUpload;
    }

    public boolean isM_isPost() {
        return m_isPost;
    }

    public boolean isM_New() {
        return m_isNew;
    }

    public boolean isM_Cas() {
        return m_isCas;
    }

    public boolean isM_QQ() {
        return m_isQQ;
    }

    public boolean isM_SinaWeibo() {
        return m_isSinaWeibo;
    }

    public boolean isM_Wechat() {
        return m_isWechat;
    }

    public InputStream getInputStream() {
        return m_inputStream;
    }

    /**
     * Get the request method.
     */
    public String getMethod() {
        return m_method;
    }

    /**
     * Add a parameter to the list of parameters
     *
     * @param name  The name of the parameter
     * @param value The value of the parameter
     */
    public void addParameter(String name, String value) {
        if (value == null)
            return;
        m_parameters.put(name, value);
    }

    public Map<String, Object> getM_parameters() {
        return m_parameters;
    }
}