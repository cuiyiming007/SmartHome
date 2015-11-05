package com.gdgl.model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

public class MyGlideModule implements GlideModule{

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		// TODO Auto-generated method stub
		builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
		builder.setDiskCache(
				  new ExternalCacheDiskCacheFactory(context, Environment.DIRECTORY_PICTURES, 50*1024*1024));
	}

	@Override
	public void registerComponents(Context arg0, Glide arg1) {
		// TODO Auto-generated method stub
		
	}

}
