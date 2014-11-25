package com.gdgl.model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.provider.BaseColumns;
import android.util.Log;

interface LibjingleSendColumns extends BaseColumns {
	public static final String GETZBNODE = "1.1";
	public static final String GETENDPOINT = "1.2";
	public static final String ZBGETZBNODECOUNT = "1.3";
	public static final String ZBGETZBNODEBYIEEE = "1.4";
	public static final String ZBGETZBNODEBYNWK_ADDR = "1.5";
	public static final String ZBGETZBNODEBYINDEX = "1.6";
	public static final String ZBGETENDPOINTCOUNT = "1.7";
	public static final String ZBGETENDPOINTBYINDEX = "1.8";

	public static final String SETPERMITJOINON = "2.1";
	public static final String DELETENODE = "2.2";
	public static final String MANAGELEAVENODE = "2.3";

	public static final String GETBINDLIST = "3.1";
	public static final String BINDDEVICE = "3.2";
	public static final String UNBINDDEVICE = "3.3";

	public static final String REMOTECONTROL = "4.1";
	public static final String ADDBINDDATA = "4.2";
	public static final String DELBINDDATA = "4.3";

	public static final String ONOFFOUTPUTOPERATION = "5.1";
	public static final String MAINSOUTLETOPERATION = "5.2";
	public static final String DIMMABLELIGHTOPERATION = "5.3";
	public static final String SHADEOPERATION = "5.4";

	public static final String BEGINLEARNIR = "6.1";
	public static final String BEGINAPPLYIR = "6.2";
	public static final String GETDEVICELEARNEDIRDATAINFORMATION = "6.3";
	public static final String DELETEIR = "6.4";

	public static final String IASZONEOPERATION = "7.1";
	public static final String GETLOCALCIELIST = "7.2";
	public static final String IASZONEWRITEIASCIEADDRESSDATA = "7.3";
	public static final String LOCALIASCIEBYPASSZONE = "7.4";
	public static final String LOCALIASCIEUNBYPASSZONE = "7.5";
	public static final String IASWARNINGDEVICEOPERATION = "7.6";
	public static final String LOCALIASCIEOPERATION = "7.7";

	public static final String TEMPERATURESENSOROPERATION = "8.1";
	public static final String LIGHTSENSOROPERATION = "8.2";

	public static final String GETALLROOMINFO = "9.1";
	public static final String GETEPBYROOMINDEX = "9.2";
	public static final String ZBADDROOMDATAMAIN = "9.3";
	public static final String ZBDELETEROOMDATAMAINBYID = "9.4";
	public static final String MODIFYDEVICEROOMID = "9.5";

	public static final String ADDDEVICECHECKMAINDATA = "10.1";
	public static final String ADDDEVICECHECKSUBDATA = "10.2";
	public static final String DELDEVICECHECKMAINDATA = "10.3";
	public static final String DELDEVICECHECKSUBDATA = "10.4";
	public static final String GETDEVICECHECKMAIN = "10.5";
	public static final String GETDEVICECHECKSUB = "10.6";

	public static final String IDENTIFYDEVICE = "13.1";
	public static final String ZBGETCHANNEL = "13.2";
	public static final String FACTORYRESET = "13.3";
	public static final String REBUILDNETWORKBYPARAM = "13.4";

}
/***
 * 通过libjingle通道发送数据后，保存在本地的数据结构
 * @author trice
 *
 */
public class LibjingleSendStructure implements LibjingleSendColumns{
	
	private int request_id;
	private int gl_msgtype;
	private String api_type;
	
	/*         闵伟ADD start      */ 
	private RequestTimer mRequestTimer;
	private ArrayList<LibjingleSendStructure> mList;  //父类List

	public LibjingleSendStructure(ArrayList<LibjingleSendStructure> list){
		mList = list; //取父类List
		mRequestTimer = new RequestTimer();
		startRequestTimer();
	}
	
	//计时器内部类
	class RequestTimer{
		private final String RequestTimerTag = "RequestTimerTag";
		private final int SEC = 5;	//计时时间5s
		private Timer mTimer;

		
		//构建并开启计时
		public RequestTimer(){
			//startTimer();
		}
		
		class RequestTimerTask extends TimerTask {
	        public void run() {
	        	stopTimer();
	        }
	    }
		//开始计时器
		public void startTimer(){
			Log.i(RequestTimerTag, "timer start.");
			mTimer = new Timer();
			mTimer.schedule(new RequestTimerTask(), SEC*1000);
		}
		
		//结束计时器
		public void stopTimer(){
			Log.i(RequestTimerTag, "timer over.");
	        mTimer.cancel(); 
		}
	}
	
/*         闵伟ADD end        */ 
	
	public int getRequest_id() {
		return request_id;
	}
	public void setRequest_id(int id) {
		request_id=id;
	}
	public int getGl_msgtype() {
		return gl_msgtype;
	}
	public void setGl_msgtype(int type) {
		gl_msgtype=type;
	}
	public String getAPI_type() {
		return api_type;
	}
	public void setAPI_type(String type) {
		api_type=type;
	}
	public void startRequestTimer(){
		mRequestTimer.startTimer();
	}
	public void	stopRequestTimer(){
		mRequestTimer.stopTimer();
	}
	public void removeLibjingleSendStructure(){
		stopRequestTimer();
		mList.remove(this);
	}
}
