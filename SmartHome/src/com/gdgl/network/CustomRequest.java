package com.gdgl.network;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.gdgl.util.UiUtils;
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
	private Map<String, String> paraMap; 

	/**
	 * GET����ʽ,ֱ�ӽ�json�ַ����Ϊ ��Ӧ��clazz����
	 * 
	 * @param url
	 *            ����url
	 * @param clazz
	 *            ������class�ֽ���
	 * @param listener
	 *            ����ɹ�������
	 * @param errorListener
	 *            ����ʧ�ܼ�����
	 */
	public CustomRequest(String url, Class<T> clazz, Listener<T> listener,
			ErrorListener errorListener) {
		this(url, null, clazz, listener, errorListener);
	}

	/**
	 * GET����ʽ,��json�е�key��Ӧ��value����Ϊ ��Ӧ��clazz����
	 * 
	 * @param url
	 *            ����url
	 * @param key
	 *            ȡ��ָ����key,<b>NOTE:</b>ֻ֧�� root-key��������key�����
	 * @param clazz
	 *            ������class�ֽ���
	 * @param listener
	 *            ����ɹ�������
	 * @param errorListener
	 *            ����ʧ�ܼ�����
	 */
	public CustomRequest(String url, String key, Class<T> clazz,
			Listener<T> listener, ErrorListener errorListener) {
		this(Method.GET, url, null, key, clazz, listener, errorListener);
	}

	/**
	 * 
	 * @param method
	 *            ���󷽷� Use {@link com.android.volley.Request.Method}.
	 * @param url
	 * @param requestBody
	 *            �����POST���󣬿����ύform�?�ַ����� name=zhangsan&age=20
	 * @param key
	 *            ȡ��ָ����key,<b>NOTE:</b>ֻ֧�� root-key��������key�����
	 * @param clazz
	 *            ������class�ֽ���
	 * @param listener
	 *            ����ɹ�������
	 * @param errorListener
	 *            ����ʧ�ܼ�����
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
			String data = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			String json=UiUtils.formatResponseString(data);
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
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
//		Map<String, String> map = new HashMap<String, String>();  
//        map.put("params1", "value1");  
//        map.put("params2", "value2");  
        return paraMap; 
	}
	public void setParamMap(HashMap<String, String> map)
	{
		paraMap=map;
	}
}