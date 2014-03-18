package com.gdgl.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gdgl.smarthome.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyVolley extends Activity implements OnClickListener{
    private Button bt_json/* bt_image, bt_netimage, bt_imageloder*/;
//    private ImageView iv_volley;
    private TextView tv_volley;
//    private NetworkImageView netIV_volley;
    private String jsonObject="{'name':'llb','age':'20'}";
    private JSONObject jsonUp;//瑕佷笂浼犵殑JSONObject
    private RequestQueue queue;
    private String jsonUrl = "http://218.192.170.251:8080/AndroidServerDemo/LoginServlet";
    //涓婇潰杩欎釜jsonUrl鏄嚜宸遍殢渚垮啓鐨勬湇鍔″櫒绔紝鍙畬鎴愮畝鍗曠殑json鏁版嵁浜や簰
private String imageUrl1 = "http://img1.27.cn/images/201011/04/1288857805_42835600.jpg";
private String imageUrl2 = "http://img.cf8.com.cn/uploadfile/2011/1031/20111031100803979.jpg";
private String imageUrl3 = "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1209/07/c1/13698570_1347000164468_320x480.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvolley);
        initView();
    }
    /**
     * 鍒濆鍖栧悇涓帶浠�     */
    private void initView() {
        bt_json = (Button) findViewById(R.id.bt_json);
//        bt_image = (Button) findViewById(R.id.bt_image);
//        bt_netimage = (Button) findViewById(R.id.bt_netimage);
//        bt_imageloder = (Button) findViewById(R.id.bt_imageloder);
//        iv_volley = (ImageView) findViewById(R.id.iv_volley);
        tv_volley = (TextView) findViewById(R.id.tv_volley);
//        netIV_volley = (NetworkImageView) findViewById(R.id.netIV_volley);

        queue = Volley.newRequestQueue(this);// 鑾峰彇涓�釜璇锋眰闃熷垪瀵硅薄

        bt_json.setOnClickListener( this);//鎸夐挳鐐瑰嚮浜嬩欢鐩戝惉
//        bt_image.setOnClickListener(this);
//        bt_netimage.setOnClickListener(this);
//        bt_imageloder.setOnClickListener(this);
    }
    /**
     * 璇锋眰json鏁版嵁
     */
    private void requestJsonObject() {
        Log.i("llb", "requestJsonObject()");
        try {
            jsonUp=new JSONObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ErrorListener errorListener=new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				 Log.i("llb", "onErrorResponse:"+arg0.getMessage());
                 tv_volley.setText("鏈嶅姟鍣ㄨ姹傚け璐ワ細"+arg0.getMessage());
			}
		};
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Method.POST, jsonUrl, jsonUp, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				// TODO Auto-generated method stub
				 Log.i("llb", "onResponse(JSONObject)");
                 String json = arg0.toString();
                 tv_volley.setText("JSON");
			}
		}, errorListener );
        Log.i("llb", "jsonUp"+jsonUp.toString());
        queue.add(jsonObjectRequest);// 娣诲姞璇锋眰鍒伴槦鍒楅噷
    }
    /*   private void requestByImageRequest() {
        Log.i("llb", "requestImage()");
        // 璇锋眰鏂规硶1锛欼mageRequest鑳藉澶勭悊鍗曞紶鍥剧墖锛岃繑鍥瀊itmap銆�        ImageRequest imageRequest = new ImageRequest(imageUrl1,
                new Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Log.i("llb","bitmap height"+response.getHeight()+"&width="+response.getWidth());
                        iv_volley.setImageBitmap(response);// 鏄剧ず鍥剧墖
                    }
                }, 200, 200, Config.ARGB_8888, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyVolley.this, "璇锋眰鍥剧墖澶辫触浜�, 0).show();
                    }
                });
        // ImageRequest涓凡缁忓啓濂戒簡缂撳瓨锛岀洿鎺ョ敤灏卞ソ浜嗭紝浣跨敤鐨勬槸DiskBasedCache
        //娴嬭瘯鍙戠幇鐩存帴鎶婂ぇ鍥炬斁鍒颁簡涓嬮潰鐨勮矾寰勯噷锛屼笂闈粎浠呮槸鏀瑰彉浜嗘樉绀烘椂鐨勫ぇ灏忥紝
        //缂撳瓨鍥剧墖澶у皬骞舵棤鍙樺寲锛屼笉瑙ｏ紵锛�        imageRequest.shouldCache();// 缂撳瓨鏂囦欢鍦�data/data/鍖呭悕/cache/volley 
        queue.add(imageRequest);// 鎶婅姹傚姞鍏ュ埌闃熷垪閲岄潰
    }
    private void requestByImageLoader() {
        // 鏂规硶浜岋細鍒╃敤ImageLoader
        ImageListener listener = ImageLoader.getImageListener(iv_volley,
                R.drawable.ic_launcher, R.drawable.error);
        //缂撳瓨鏂囦欢涔熸斁鍦�data/data/鍖呭悕/cache/volley锛岀紦瀛樺浘鐗囧ぇ灏忓苟鏃犲彉鍖�        ImageLoader loader = new ImageLoader(queue, new MyImageCache(5 * 1024 * 1024));
        loader.get(imageUrl2, listener, 300, 300);// 鑾峰彇鍥剧墖 
        //鏈�悗杩樻槸璋冪敤ImageRequest閲岄潰鐨刣oParse()鍑芥暟鍘昏姹傜綉缁�    }
    private void requestByNetworkImageView() {
        // 鏂规硶涓夛細鍒╃敤NetworkImageView鏉ヨ姹傚浘鐗�        ImageLoader imageLoader = new ImageLoader(queue, new MyImageCache(5 * 1024 * 1024));
        netIV_volley.setDefaultImageResId(R.drawable.ic_launcher);//榛樿鍥剧墖
        netIV_volley.setErrorImageResId(R.drawable.error);//鍑洪敊鏃剁殑鍥剧墖
//      public ImageContainer get(String requestUrl, final ImageListener listener) {
//            return get(requestUrl, listener, 0, 0);
//       }鏈�悗浜嬪疄涓婅皟鐢ㄧ殑鏄笂闈㈣繖涓狪mageLoader閲岄潰鐨勮繖涓嚱鏁帮紝鑰屼笖鏄粯璁ゅ昂瀵革紝绠楁槸涓�釜缂洪櫡锛燂紵锛�        netIV_volley.setImageUrl(imageUrl3, imageLoader);//璇锋眰鍥剧墖
    }*/
    @Override
    public void onClick(View v) {   // 鎸夐挳鍝嶅簲
        switch (v.getId()) {
        case R.id.bt_json:// 璇锋眰json
            requestJsonObject();
            break;
      /*  case R.id.bt_image:// 鍒╃敤ImageRequest璇锋眰鍥剧墖
            requestByImageRequest();
            break;
        case R.id.bt_imageloder:// 鍒╃敤ImageLoader璇锋眰鍥剧墖
            requestByImageLoader();
            break;
        case R.id.bt_netimage:
            requestByNetworkImageView();
            break;*/
        }
    }
}
