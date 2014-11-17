package com.gdgl.model;

import java.io.Serializable;

import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.DevParam;
import com.gdgl.mydata.Node;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.util.UiUtils;

import android.content.ContentValues;
import android.provider.BaseColumns;

interface DevicesBaseColumns extends BaseColumns {

	public static final String ALL_COUNT = "allcount";
	public static final String CURCOUNT = "curcount";
	public static final String DEVICE_ID = "device_id";
	public static final String R_ID = "rid";
	public static final String PIC_NAME = "picname";
	public static final String PROFILE_ID = "profileid";
	public static final String POWER_RESOURCE = "powersource";
	public static final String CUR_POWER_RESOURCE = "curpowersource";
	public static final String CURPOWERSOURCELEVEL = "curpowersourcelevel";

	public static final String IEEE = "ieee";
	public static final String NWK_ADDR = "nwk_addr";
	public static final String NODE_EN_NAME = "node_en_name";
	public static final String MANUFACTORY = "manufactory";
	public static final String ZCL_VERSTION = "zcl_version";
	public static final String STACK_VERSTION = "stack_version";
	public static final String APP_VERSTION = "app_version";
	public static final String HW_VERSTION = "hw_version";
	public static final String DATE_CODE = "date_code";
	public static final String MODEL_ID = "model_id";
	public static final String NODE_TYPE = "node_type";

	public static final String EP = "ep";
	public static final String NAME = "name";
	public static final String CURRENT = "current";
	public static final String ENERGY = "energy";
	public static final String POWER = "power";
	public static final String VOLTAGE = "voltage";
	public static final String LEVEL = "level";
	public static final String ON_OFF_STATUS = "on_off_status";
	public static final String TEMPERATURE = "temperature";
	public static final String HUMIDITY = "humidity";
	public static final String BRIGHTNESS = "brightness";
	public static final String EP_MODEL_ID = "ep_model_id";

	public static final String CURRENT_MIN = "current_min";
	public static final String CURRENT_MAX = "current_max";
	public static final String VOLTAGE_MIN = "voltage_min";
	public static final String VOLTAGE_MAX = "voltage_max";
	public static final String ENERGY_MIN = "energy_min";
	public static final String ENERGY_MAX = "energy_max";

	// 自定义
	public static final String CLUSTER_ID = "cluster_id";
	public static final String DEVICE_SORT = "device_sort";
	public static final String DEVICE_REGION = "device_region";
	public static final String DEFAULT_DEVICE_NAME = "default_device_name";

	public static final String LAST_UPDATE_TIME = "last_update_time";
	public static final String ON_OFF_LINE = "on_off_line";

}

public class DevicesModel implements DevicesBaseColumns, Serializable {

	/**
	 * serialVersionUID作用： 序列化时为了保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性。
	 */
	private static final long serialVersionUID = -8126838294738799039L;

	public static final int DEVICE_ON_LINE = 1;
	public static final int DEVICE_OFF_LINE = 0;

	private int ID;
	private String mAllCount = "";
	private String mCurCount = "";
	private int mDeviceId;
	private String mRid = "-1";
	private String mPicName = "";
	private String mProfileId = "0104";
	private String mPowerResource = "";
	private String mCurPowerResource = "";
	private String curpowersourcelevel = "";

	private String mIeee = "";
	private String mNWKAddr = "";
	private String mNodeENNAme = "";
	private String mManufactory = "";
	private String mZCLVersion = "";
	private String mStackVerstion = "stack_version";
	private String mAppVersion = "";
	private String mHwVersion = "";
	private String mDateCode = "";
	private String mModelId = "";
	private String mNodeType = "";

	private String mEP = "";
	private String mName = "";
	private String mCurrent = "";
	private String mEnergy = "";
	private String mPower = "";
	private String mVoltage = "";
	private String mlevel = "";
	private String mOnOffStatus = "";
	private float mTemperature;
	private float mHumidity;
	private int mBrightness;
	private String mEPModelId = "";

	private String mCurrentMin = "";
	private String mCurrentMax = "";
	private String mVoltageMin = "";
	private String mVoltageMax = "";
	private String mEnergyMin = "";
	private String mEnergyMax = "";

	// 自定义
	private String mClusterID = "";
	private int mDeviceSort;
	private String mDeviceRegion = "";
	private String mDefaultDeviceName = "";

	private long mLastDateTime;
	private int mOnOffLine = DEVICE_ON_LINE;

	private int mValue1 = 0;
	private int mValue2 = 0;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getmAllCount() {
		return mAllCount;
	}

	public void setmAllCount(String mAllCount) {
		this.mAllCount = mAllCount;
	}

	public String getmCurCount() {
		return mCurCount;
	}

	public void setmCurCount(String mCurCount) {
		this.mCurCount = mCurCount;
	}

	public int getmDeviceId() {
		return mDeviceId;
	}

	public void setmDeviceId(int mDeviceId) {
		this.mDeviceId = mDeviceId;
	}

	public String getmRid() {
		return mRid;
	}

	public void setmRid(String mRid) {
		this.mRid = mRid;
	}

	public String getmPicName() {
		return mPicName;
	}

	public void setmPicName(String mPicName) {
		this.mPicName = mPicName;
	}

	public String getmProfileId() {
		return mProfileId;
	}

	public void setmProfileId(String mProfileId) {
		this.mProfileId = mProfileId;
	}

	public String getmPowerResource() {
		return mPowerResource;
	}

	public void setmPowerResource(String mPowerResource) {
		this.mPowerResource = mPowerResource;
	}

	public String getmCurPowerResource() {
		return mCurPowerResource;
	}

	public void setmCurPowerResource(String mCurPowerResource) {
		this.mCurPowerResource = mCurPowerResource;
	}

	public String getmIeee() {
		return mIeee;
	}

	public void setmIeee(String mIeee) {
		this.mIeee = mIeee;
	}

	public String getmNWKAddr() {
		return mNWKAddr;
	}

	public void setmNWKAddr(String mNWKAddr) {
		this.mNWKAddr = mNWKAddr;
	}

	public String getmNodeENNAme() {
		return mNodeENNAme;
	}

	public void setmNodeENNAme(String mNodeENNAme) {
		this.mNodeENNAme = mNodeENNAme;
	}

	public String getmManufactory() {
		return mManufactory;
	}

	public void setmManufactory(String mManufactory) {
		this.mManufactory = mManufactory;
	}

	public String getmZCLVersion() {
		return mZCLVersion;
	}

	public void setmZCLVersion(String mZCLVersion) {
		this.mZCLVersion = mZCLVersion;
	}

	public String getmStackVerstion() {
		return mStackVerstion;
	}

	public void setmStackVerstion(String mStackVerstion) {
		this.mStackVerstion = mStackVerstion;
	}

	public String getmAppVersion() {
		return mAppVersion;
	}

	public void setmAppVersion(String mAppVersion) {
		this.mAppVersion = mAppVersion;
	}

	public String getmHwVersion() {
		return mHwVersion;
	}

	public void setmHwVersion(String mHwVersion) {
		this.mHwVersion = mHwVersion;
	}

	public String getmDateCode() {
		return mDateCode;
	}

	public void setmDateCode(String mDateCode) {
		this.mDateCode = mDateCode;
	}

	public String getmModelId() {
		return mModelId;
	}

	public void setmModelId(String mModelId) {
		this.mModelId = mModelId;
	}

	public String getmNodeType() {
		return mNodeType;
	}

	public void setmNodeType(String mNodeType) {
		this.mNodeType = mNodeType;
	}

	public String getmEP() {
		return mEP;
	}

	public void setmEP(String mEP) {
		this.mEP = mEP;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmCurrent() {
		return mCurrent;
	}

	public void setmCurrent(String mCurrent) {
		this.mCurrent = mCurrent;
	}

	public String getmEnergy() {
		return mEnergy;
	}

	public void setmEnergy(String mEnergy) {
		this.mEnergy = mEnergy;
	}

	public String getmPower() {
		return mPower;
	}

	public void setmPower(String mPower) {
		this.mPower = mPower;
	}

	public String getmVoltage() {
		return mVoltage;
	}

	public void setmVoltage(String mVoltage) {
		this.mVoltage = mVoltage;
	}

	public String getmLevel() {
		return mlevel;
	}

	public void setmLevel(String level) {
		this.mlevel = level;
	}

	public String getmOnOffStatus() {
		return mOnOffStatus;
	}

	public void setmOnOffStatus(String mOnOffStatus) {
		this.mOnOffStatus = mOnOffStatus;
	}

	public float getmTemperature() {
		return mTemperature;
	}

	public void setmTemperature(float temp) {
		this.mTemperature = temp;
	}

	public float getmHumidity() {
		return mHumidity;
	}

	public void setmHumidity(float hum) {
		this.mHumidity = hum;
	}

	public int getmBrightness() {
		return mBrightness;
	}

	public void setmBrightness(int brightness) {
		this.mBrightness = brightness;
	}

	public String getmEPModelId() {
		return mEPModelId;
	}

	public void setmEPModelId(String mEPModelId) {
		this.mEPModelId = mEPModelId;
	}

	public String getmCurrentMin() {
		return mCurrentMin;
	}

	public void setmCurrentMin(String mCurrentMin) {
		this.mCurrentMin = mCurrentMin;
	}

	public String getmCurrentMax() {
		return mCurrentMax;
	}

	public void setmCurrentMax(String mCurrentMax) {
		this.mCurrentMax = mCurrentMax;
	}

	public String getmVoltageMin() {
		return mVoltageMin;
	}

	public void setmVoltageMin(String mVoltageMin) {
		this.mVoltageMin = mVoltageMin;
	}

	public String getmVoltageMax() {
		return mVoltageMax;
	}

	public void setmVoltageMax(String mVoltageMax) {
		this.mVoltageMax = mVoltageMax;
	}

	public String getmEnergyMin() {
		return mEnergyMin;
	}

	public void setmEnergyMin(String mEnergyMin) {
		this.mEnergyMin = mEnergyMin;
	}

	public String getmEnergyMax() {
		return mEnergyMax;
	}

	public void setmEnergyMax(String mEnergyMax) {
		this.mEnergyMax = mEnergyMax;
	}

	public String getmClusterID() {
		return mClusterID;
	}

	public void setmClusterID(String mClusterID) {
		this.mClusterID = mClusterID;
	}

	public int getmDeviceSort() {
		return mDeviceSort;
	}

	public void setmDeviceSort(int devicesort) {
		this.mDeviceSort = devicesort;
	}

	public String getmDeviceRegion() {
		return mDeviceRegion;
	}

	public void setmDeviceRegion(String mDeviceRegion) {
		this.mDeviceRegion = mDeviceRegion;
	}

	public long getmLastDateTime() {
		return mLastDateTime;
	}

	public void setmLastDateTime(long mLastDateTime) {
		this.mLastDateTime = mLastDateTime;
	}

	public int getmOnOffLine() {
		return mOnOffLine;
	}

	public void setmOnOffLine(int mOnOffLine) {
		this.mOnOffLine = mOnOffLine;
	}

	public String getmDefaultDeviceName() {
		return mDefaultDeviceName;
	}

	public void setmDefaultDeviceName(String mUserDefineName) {
		this.mDefaultDeviceName = mUserDefineName;
	}

	public int getmValue1() {
		return mValue1;
	}

	public void setmValue1(int mValue1) {
		this.mValue1 = mValue1;
	}

	public int getmValue2() {
		return mValue2;
	}

	public void setmValue2(int mValue2) {
		this.mValue2 = mValue2;
	}

	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(DevicesBaseColumns.ALL_COUNT, getmAllCount());
		mContentValues.put(DevicesBaseColumns.CURCOUNT, getmCurCount());
		mContentValues.put(DevicesBaseColumns.DEVICE_ID, getmDeviceId());
		mContentValues.put(DevicesBaseColumns.R_ID, getmRid());
		mContentValues.put(DevicesBaseColumns.PIC_NAME, getmPicName());
		mContentValues.put(DevicesBaseColumns.PROFILE_ID, getmProfileId());
		mContentValues.put(DevicesBaseColumns.POWER_RESOURCE,
				getmPowerResource());
		mContentValues.put(DevicesBaseColumns.CUR_POWER_RESOURCE,
				getmCurPowerResource());
		mContentValues.put(DevicesBaseColumns.CURPOWERSOURCELEVEL,
				getCurpowersourcelevel());

		mContentValues.put(DevicesBaseColumns.IEEE, getmIeee());
		mContentValues.put(DevicesBaseColumns.NWK_ADDR, getmNWKAddr());
		mContentValues.put(DevicesBaseColumns.NODE_EN_NAME, getmNodeENNAme());
		mContentValues.put(DevicesBaseColumns.MANUFACTORY, getmManufactory());
		mContentValues.put(DevicesBaseColumns.ZCL_VERSTION, getmZCLVersion());
		mContentValues.put(DevicesBaseColumns.STACK_VERSTION,
				getmStackVerstion());
		mContentValues.put(DevicesBaseColumns.APP_VERSTION, getmAppVersion());
		mContentValues.put(DevicesBaseColumns.HW_VERSTION, getmHwVersion());
		mContentValues.put(DevicesBaseColumns.DATE_CODE, getmDateCode());
		mContentValues.put(DevicesBaseColumns.MODEL_ID, getmModelId());
		mContentValues.put(DevicesBaseColumns.NODE_TYPE, getmNodeType());

		mContentValues.put(DevicesBaseColumns.EP, getmEP());
		mContentValues.put(DevicesBaseColumns.NAME, getmName());
		mContentValues.put(DevicesBaseColumns.CURRENT, getmCurrent());
		mContentValues.put(DevicesBaseColumns.ENERGY, getmEnergy());
		mContentValues.put(DevicesBaseColumns.POWER, getmPower());
		mContentValues.put(DevicesBaseColumns.VOLTAGE, getmVoltage());
		mContentValues.put(DevicesBaseColumns.LEVEL, getmLevel());
		mContentValues.put(DevicesBaseColumns.ON_OFF_STATUS, getmOnOffStatus());
		mContentValues.put(DevicesBaseColumns.TEMPERATURE, getmTemperature());
		mContentValues.put(DevicesBaseColumns.HUMIDITY, getmHumidity());
		mContentValues.put(DevicesBaseColumns.BRIGHTNESS, getmBrightness());
		mContentValues.put(DevicesBaseColumns.EP_MODEL_ID, getmEPModelId());

		mContentValues.put(DevicesBaseColumns.CURRENT_MAX, getmCurrentMax());
		mContentValues.put(DevicesBaseColumns.CURRENT_MIN, getmCurrentMin());
		mContentValues.put(DevicesBaseColumns.VOLTAGE_MAX, getmVoltageMax());
		mContentValues.put(DevicesBaseColumns.VOLTAGE_MIN, getmVoltageMin());
		mContentValues.put(DevicesBaseColumns.ENERGY_MAX, getmEnergyMax());
		mContentValues.put(DevicesBaseColumns.ENERGY_MIN, getmEnergyMin());

		mContentValues.put(DevicesBaseColumns.CLUSTER_ID, getmClusterID());
		mContentValues.put(DevicesBaseColumns.DEVICE_SORT, getmDeviceSort());
		mContentValues
				.put(DevicesBaseColumns.DEVICE_REGION, getmDeviceRegion());
		mContentValues.put(DevicesBaseColumns.DEFAULT_DEVICE_NAME,
				getmDefaultDeviceName());

		mContentValues.put(DevicesBaseColumns.LAST_UPDATE_TIME,
				getmLastDateTime());
		mContentValues.put(DevicesBaseColumns.ON_OFF_LINE, getmOnOffLine());

		return mContentValues;
	}

	public DevicesModel() {
	}

	// ResponseParamsEndPoint
	public DevicesModel(ResponseParamsEndPoint r) {

		DevParam d = r.getDevparam();
		Node n = d.getNode();

		setmAllCount(r.getAllcount());
		setmCurCount(r.getCurcount());
		setmDeviceId(r.getDevice_id() == null ? -1 : Integer.parseInt(r
				.getDevice_id()));
		setmRid(r.getRid());
		setmPicName(r.getPicname());
		setmProfileId(r.getProfileid());
		setmPowerResource(r.getPowersource());
		setmCurPowerResource(r.getCurpowersource());
		setCurpowersourcelevel(r.getCurpowersourcelevel());

		setmIeee(n.getIeee() == null ? "" : n.getIeee());
		setmNWKAddr(n.getNwk_addr() == null ? "" : n.getNwk_addr());
		setmNodeENNAme(n.getName() == null ? "" : n.getName());
		setmManufactory(n.getManufactory());
		setmZCLVersion(n.getZcl_version());
		setmStackVerstion(n.getStack_version());
		setmAppVersion(n.getApp_version());
		setmHwVersion(n.getHw_version());
		setmDateCode(n.getDate_code());
		setmModelId(n.getModel_id() == null ? "" : n.getModel_id());
		setmNodeType(n.getNode_type() == null ? "" : n.getNode_type());

		setmEP(d.getEp() == null ? "" : d.getEp());
		setmName(d.getName() == null ? "" : d.getName());
		setmCurrent(d.getCurrent());
		setmEnergy(d.getEnergy());
		setmPower(d.getPower());
		setmVoltage(d.getVoltage());
		setmLevel(d.getLevel());
		setmOnOffStatus(d.getOn_off_status() == null ? "0" : d
				.getOn_off_status());
		setmTemperature(d.getTemp()==null?-100:Integer.parseInt(d.getTemp())/10000);
		setmHumidity(d.getHum()==null?-100:Integer.parseInt(d.getHum()));
		setmBrightness(d.getBrightness()==null?-100:Integer.parseInt(d.getBrightness()));
		setmEPModelId(d.getEp_model_id() == null ? "" : d.getEp_model_id());

		setmCurrentMax(d.getCurrentmax());
		setmCurrentMin(d.getCurrentmin());
		setmVoltageMax(d.getVoltagemax());
		setmVoltageMin(d.getVoltagemin());
		setmEnergyMax(d.getEnergymax());
		setmEnergyMin(d.getEnergymin());

		setmClusterID(DataUtil.getClusterIdByDeviceid_Modelid(n.getModel_id(),
				d.getEp()));
		setmDeviceSort(DataUtil.getDefaultDevicesSort(
				Integer.parseInt(r.getDevice_id()), n.getModel_id()));
		setmDeviceRegion("");
		setmDefaultDeviceName(DataUtil.getDefaultDevicesName(
				ApplicationController.getInstance(), getmModelId(), getmEP()));

		setmLastDateTime(System.currentTimeMillis());
		setmOnOffLine(DEVICE_ON_LINE);
	}

	@Override
	public String toString() {
		return "DevicesModel [ID=" + ID + ", mAllCount=" + mAllCount
				+ ", mCurCount=" + mCurCount + ", mDeviceId=" + mDeviceId
				+ ", mRid=" + mRid + ", mPicName=" + mPicName + ", mProfileId="
				+ mProfileId + ", mPowerResource=" + mPowerResource
				+ ", mCurPowerResource=" + mCurPowerResource + ", mIeee="
				+ mIeee + ", mNWKAddr=" + mNWKAddr + ", mNodeENNAme="
				+ mNodeENNAme + ", mClusterID=" + mClusterID
				+ ", mManufactory=" + mManufactory + ", mZCLVersion="
				+ mZCLVersion + ", mStackVerstion=" + mStackVerstion
				+ ", mAppVersion=" + mAppVersion + ", mHwVersion=" + mHwVersion
				+ ", mDateCode=" + mDateCode + ", mModelId=" + mModelId
				+ ", mNodeType=" + mNodeType + ", mEP=" + mEP + ", mName="
				+ mName + ", mCurrent=" + mCurrent + ", mEnergy=" + mEnergy
				+ ", mPower=" + mPower + ", mVoltage=" + mVoltage
				+ ", mOnOffStatus=" + mOnOffStatus + ", mEPModelId="
				+ mEPModelId + ", mCurrentMin=" + mCurrentMin
				+ ", mCurrentMax=" + mCurrentMax + ", mVoltageMin="
				+ mVoltageMin + ", mVoltageMax=" + mVoltageMax
				+ ", mEnergyMin=" + mEnergyMin + ", mEnergyMax=" + mEnergyMax
				+ ", mDeviceRegion=" + mDeviceRegion + ", mLastDateTime="
				+ mLastDateTime + ", mOnOffLine=" + mOnOffLine
				+ ", mDefaultDeviceName=" + mDefaultDeviceName + ", mValue1="
				+ mValue1 + ", mValue2=" + mValue2 + "]";
	}

	public String getCurpowersourcelevel() {
		return curpowersourcelevel;
	}

	public void setCurpowersourcelevel(String curpowersourcelevel) {
		this.curpowersourcelevel = curpowersourcelevel;
	}

}
