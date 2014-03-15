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
    private JSONObject jsonUp;//要上传的JSONObject
    private RequestQueue queue;
    private String jsonUrl = "http://218.192.170.251:8080/AndroidServerDemo/LoginServlet";
    //上面这个jsonUrl是自己随便写的服务器端，只完成简单的json数据交互
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
     * 初始化各个控件
     */
    private void initView() {
        bt_json = (Button) findViewById(R.id.bt_json);
//        bt_image = (Button) findViewById(R.id.bt_image);
//        bt_netimage = (Button) findViewById(R.id.bt_netimage);
//        bt_imageloder = (Button) findViewById(R.id.bt_imageloder);
//        iv_volley = (ImageView) findViewById(R.id.iv_volley);
        tv_volley = (TextView) findViewById(R.id.tv_volley);
//        netIV_volley = (NetworkImageView) findViewById(R.id.netIV_volley);

        queue = Volley.newRequestQueue(this);// 获取一个请求队列对象

        bt_json.setOnClickListener( this);//按钮点击事件监听
//        bt_image.setOnClickListener(this);
//        bt_netimage.setOnClickListener(this);
//        bt_imageloder.setOnClickListener(this);
    }
    /**
     * 请求json数据
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
                 tv_volley.setText("服务器请求失败："+arg0.getMessage());
			}
		};
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Method.POST, jsonUrl, jsonUp, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				// TODO Auto-generated method stub
				 Log.i("llb", "onResponse(JSONObject)");
                 String json = arg0.toString();
                 tv_volley.setText("服务器返回的json数据："+json);
			}
		}, errorListener );
        Log.i("llb", "jsonUp"+jsonUp.toString());
        queue.add(jsonObjectRequest);// 添加请求到队列里
    }
    /*   private void requestByImageRequest() {
        Log.i("llb", "requestImage()");
        // 请求方法1：ImageRequest能够处理单张图片，返回bitmap。
        ImageRequest imageRequest = new ImageRequest(imageUrl1,
                new Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Log.i("llb","bitmap height"+response.getHeight()+"&width="+response.getWidth());
                        iv_volley.setImageBitmap(response);// 显示图片
                    }
                }, 200, 200, Config.ARGB_8888, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyVolley.this, "请求图片失败了", 0).show();
                    }
                });
        // ImageRequest中已经写好了缓存，直接用就好了，使用的是DiskBasedCache
        //测试发现直接把大图放到了下面的路径里，上面仅仅是改变了显示时的大小，
        //缓存图片大小并无变化，不解？？
        imageRequest.shouldCache();// 缓存文件在/data/data/包名/cache/volley 
        queue.add(imageRequest);// 把请求加入到队列里面
    }
    private void requestByImageLoader() {
        // 方法二：利用ImageLoader
        ImageListener listener = ImageLoader.getImageListener(iv_volley,
                R.drawable.ic_launcher, R.drawable.error);
        //缓存文件也放在/data/data/包名/cache/volley，缓存图片大小并无变化
        ImageLoader loader = new ImageLoader(queue, new MyImageCache(5 * 1024 * 1024));
        loader.get(imageUrl2, listener, 300, 300);// 获取图片 
        //最后还是调用ImageRequest里面的doParse()函数去请求网络
    }
    private void requestByNetworkImageView() {
        // 方法三：利用NetworkImageView来请求图片
        ImageLoader imageLoader = new ImageLoader(queue, new MyImageCache(5 * 1024 * 1024));
        netIV_volley.setDefaultImageResId(R.drawable.ic_launcher);//默认图片
        netIV_volley.setErrorImageResId(R.drawable.error);//出错时的图片
//      public ImageContainer get(String requestUrl, final ImageListener listener) {
//            return get(requestUrl, listener, 0, 0);
//       }最后事实上调用的是上面这个ImageLoader里面的这个函数，而且是默认尺寸，算是一个缺陷？？？
        netIV_volley.setImageUrl(imageUrl3, imageLoader);//请求图片
    }*/
    @Override
    public void onClick(View v) {   // 按钮响应
        switch (v.getId()) {
        case R.id.bt_json:// 请求json
            requestJsonObject();
            break;
      /*  case R.id.bt_image:// 利用ImageRequest请求图片
            requestByImageRequest();
            break;
        case R.id.bt_imageloder:// 利用ImageLoader请求图片
            requestByImageLoader();
            break;
        case R.id.bt_netimage:
            requestByNetworkImageView();
            break;*/
        }
    }
}
