package com.gdgl.network;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;


/**
 * CustomRequest.java
 * custom a volley request for parse all kinds of JSON string to object
 * 
 */
public class CustomRequest<T> extends JsonRequest<T> {
	private Gson gson;
	private Class<T> clazz;
	private String mKey;

	/**
	 * GET请求方式,直接将json字符串解析为 对应的clazz对象
	 * 
	 * @param url
	 *            请求url
	 * @param clazz
	 *            解析的class字节码
	 * @param listener
	 *            请求成功监听器
	 * @param errorListener
	 *            请求失败监听器
	 */
	public CustomRequest(String url, Class<T> clazz, Listener<T> listener,
			ErrorListener errorListener) {
		this(url, null, clazz, listener, errorListener);
	}

	/**
	 * GET请求方式,将json中的key对应的value解析为 对应的clazz对象
	 * 
	 * @param url
	 *            请求url
	 * @param key
	 *            取得指定的key,<b>NOTE:</b>只支持 root-key，所有子key均错误
	 * @param clazz
	 *            解析的class字节码
	 * @param listener
	 *            请求成功监听器
	 * @param errorListener
	 *            请求失败监听器
	 */
	public CustomRequest(String url, String key, Class<T> clazz,
			Listener<T> listener, ErrorListener errorListener) {
		this(Method.GET, url, null, key, clazz, listener, errorListener);
	}

	/**
	 * 
	 * @param method
	 *            请求方法 Use {@link com.android.volley.Request.Method}.
	 * @param url
	 * @param requestBody
	 *            如果是POST请求，可以提交form表单字符串，比如 name=zhangsan&age=20
	 * @param key
	 *            取得指定的key,<b>NOTE:</b>只支持 root-key，所有子key均错误
	 * @param clazz
	 *            解析的class字节码
	 * @param listener
	 *            请求成功监听器
	 * @param errorListener
	 *            请求失败监听器
	 */
	public CustomRequest(int method, String url, String requestBody,
			String key, Class<T> clazz, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, null, listener, errorListener);

		this.clazz = clazz;
		mKey = key;
		gson = new Gson();

	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			T t = null;
			if(mKey == null){
				t = gson.fromJson(json, clazz);
			}else{
				JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
				t = gson.fromJson(jsonObject.get(mKey), clazz);
			}
			return Response.success(t,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}