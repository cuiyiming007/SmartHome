package com.gdgl.network;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

public class StringRequestChina extends StringRequest {

	public StringRequestChina(int method, String url,
            Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        // TODO Auto-generated constructor stub
    }
	
	public StringRequestChina(String url,
            Listener<String> listener, ErrorListener errorListener) {
		this(Method.GET,url, listener, errorListener);
        // TODO Auto-generated constructor stub
    }
	
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		String str = null;
        try {
            str = new String(response.data,"utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
	}
}
