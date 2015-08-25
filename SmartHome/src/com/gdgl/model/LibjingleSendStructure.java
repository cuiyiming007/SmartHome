package com.gdgl.model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.provider.BaseColumns;
import android.util.Log;

interface LibjingleSendColumns extends BaseColumns {
	public static final int GETNEWJOINNETDEVICE = 10;
	public static final int GETZBNODE = 11;
	public static final int GETENDPOINT = 12;
	public static final int ZBGETZBNODECOUNT = 13;
	public static final int ZBGETZBNODEBYIEEE = 14;
	public static final int ZBGETZBNODEBYNWK_ADDR = 15;
	public static final int ZBGETZBNODEBYINDEX = 16;
	public static final int ZBGETENDPOINTCOUNT = 17;
	public static final int ZBGETENDPOINTBYINDEX = 18;

	public static final int SETALLPERMITJOINON =20;
	public static final int SETPERMITJOINON = 21;
	public static final int DELETENODE = 22;
	public static final int MANAGELEAVENODE = 23;

	public static final int GETALLBINDLIST = 30;
	public static final int GETBINDLIST = 31;
	public static final int BINDDEVICE = 32;
	public static final int UNBINDDEVICE = 33;

	public static final int REMOTECONTROL = 41;
	public static final int ADDBINDDATA = 42;
	public static final int DELBINDDATA = 43;

	public static final int ONOFFOUTPUTOPERATION = 51;
	public static final int MAINSOUTLETOPERATION = 52;
	public static final int DIMMABLELIGHTOPERATION = 53;
	public static final int SHADEOPERATION = 54;

	public static final int BEGINLEARNIR = 61;
	public static final int BEGINAPPLYIR = 62;
	public static final int GETDEVICELEARNEDIRDATAINFORMATION = 63;
	public static final int DELETEIR = 64;

	public static final int IASZONEOPERATION = 71;
	public static final int GETLOCALCIELIST = 72;
	public static final int IASZONEWRITEIASCIEADDRESSDATA = 73;
	public static final int LOCALIASCIEBYPASSZONE = 74;
	public static final int LOCALIASCIEUNBYPASSZONE = 75;
	public static final int IASWARNINGDEVICEOPERATION = 76;
	public static final int LOCALIASCIEOPERATION = 77;
	public static final int GETLOCALIASCIEOPERATION = 78;
	
	public static final int HUMIDITY = 80;
	public static final int TEMPERATURESENSOROPERATION = 81;
	public static final int LIGHTSENSOROPERATION = 82;

	public static final int GETALLROOMINFO = 91;
	public static final int GETEPBYROOMINDEX = 92;
	public static final int ZBADDROOMDATAMAIN = 93;
	public static final int ZBDELETEROOMDATAMAINBYID = 94;
	public static final int MODIFYDEVICEROOMID = 95;

	public static final int ADDDEVICECHECKMAINDATA = 101;
	public static final int ADDDEVICECHECKSUBDATA = 102;
	public static final int DELDEVICECHECKMAINDATA = 103;
	public static final int DELDEVICECHECKSUBDATA = 104;
	public static final int GETDEVICECHECKMAIN = 105;
	public static final int GETDEVICECHECKSUB = 106;
	
	public static final int READHEARTTIME = 130;
	public static final int IDENTIFYDEVICE = 131;
	public static final int ZBGETCHANNEL = 132;
	public static final int FACTORYRESET = 133;
	public static final int REBUILDNETWORKBYPARAM = 134;
	
	public static final int GETVIDEOLIST = 200;
	public static final int REQUESTVIDEO = 201;
	
	public static final int DONOTCARE = 210;
	public static final int GETSCENELIST = 211;
	public static final int GETLINKAGELIST = 212;
	public static final int GETTIMEACTIONLIST = 213;
	
	
	public static final int GETRFDEVICELIST = 220;
	public static final int GATEWAYAUTH = 230;

}
/***
 * 通过libjingle通道发送数据后，保存在本地的数据结构
 * @author trice
 *
 */
public class LibjingleSendStructure implements LibjingleSendColumns{
	
	private int request_id;
	private int gl_msgtype;
	private int api_type;
	
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
		private final int SEC = 60;	//计时时间5s
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
	public int getAPI_type() {
		return api_type;
	}
	public void setAPI_type(int type) {
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
