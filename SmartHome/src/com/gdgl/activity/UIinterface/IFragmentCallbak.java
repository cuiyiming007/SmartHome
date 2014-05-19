package com.gdgl.activity.UIinterface;

import android.R.integer;

public interface IFragmentCallbak {
	/***
	 * 
	 * @param requsetId  区别这是什么操作
	 * @param result     干完结果正确否
	 * @param data       要返回的数据
	 */
	public void onFragmentResult(int requsetId,boolean result,Object data);

}
