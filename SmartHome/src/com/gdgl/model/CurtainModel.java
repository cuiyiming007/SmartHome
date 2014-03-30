package com.gdgl.model;

public class CurtainModel {

    private int mId;
    private String mName;
    private String mRegion;
    private double mLightLevel = 0.0;

    public CurtainModel(int id, String name, String region) {
        // TODO Auto-generated constructor stub
        this(id, name, region, 0.0);
    }

    public CurtainModel(int id, String name, String region, double lightLevel) {
        // TODO Auto-generated constructor stub
        mId = id;
        mName = name;
        mRegion = region;
        mLightLevel = lightLevel;
    }

    public void setID(int id) {
        if (id > 0) {
            mId = id;
        }

    }

    public void setName(String name) {
        if (null != name) {
            mName = name;
        }

    }

    public void setRegion(String region) {
        if (null != region) {
            mRegion = region;
        }

    }

    public void setLevel(double level) {
        mLightLevel = level;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getRegion() {
        return mRegion;
    }

    public double getLevel() {
        return mLightLevel;
    }
}
