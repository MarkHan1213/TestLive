package com.mark.testtx.net;

import org.json.JSONObject;

public interface IJSONResponseCallback 
{
	/**
     * The actual callback method
     * @param rid The request id.
     * @param response An Node located at a &lt;response> element.
     * @param Additional data to be passed with the call.
     */
    public void handleResponse(JSONObject response);
    
    /**
     * Called when a network error has occurred
     */
    public void handleError(int code);
}
