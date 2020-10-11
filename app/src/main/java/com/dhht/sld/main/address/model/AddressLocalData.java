package com.dhht.sld.main.address.model;

import java.util.ArrayList;

public class AddressLocalData {
    private static AddressLocalData addressLocalData;
    private int type;
    // 起点的信息
    private int startId;
    private String province;
    private String area;
    private double latitude;
    private double longitude;
    private String userName;
    private String phone;
    // 终点的信息
    private int endId;
    private String endProvince;
    private String endArea;
    private double endLatitude;
    private double endLongitude;
    private String endUserName;
    private String endPhone;
    // 物品信息
    private int articleId;
    private String articleName;
    private String articleInfo;
    public ArrayList<String> pictureUrlList;


    private AddressLocalData() {}

    public static AddressLocalData getInstance() {
        addressLocalData = AddressLocalDataHolder.INSTANCE;
        return addressLocalData;
    }

    public void clear() {
        endId = 0;
        endProvince = null;
        endArea = null;
        endLatitude = 0;
        endLongitude = 0;
        endUserName = null;
        endPhone = null;
    }

    private static class AddressLocalDataHolder{
        private static AddressLocalData INSTANCE = new AddressLocalData();
    }

    public AddressLocalData setType(int type)
    {
        this.type = type;
        return this;
    }

    public AddressLocalData setStartAll(String province, String area, double latitude, double longitude)
    {
        this.province = province;
        this.area = area;
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }

    public AddressLocalData setEndAll(String province, String area, double latitude, double longitude)
    {
        this.endProvince = province;
        this.endArea = area;
        this.endLatitude = latitude;
        this.endLongitude = longitude;
        return this;
    }

    public AddressLocalData setArticle(int articleId, String articleName, String articleInfo, ArrayList<String> pictureUrlList)
    {
        this.articleId = articleId;
        this.articleName = articleName;
        this.articleInfo = articleInfo;
        this.pictureUrlList=pictureUrlList;
        return this;
    }

    public AddressLocalData setStartUser(String name, String phone)
    {
        this.userName = name;
        this.phone = phone;
        return this;
    }

    public AddressLocalData setEndUser(String name, String phone)
    {
        this.endUserName = name;
        this.endPhone = phone;
        return this;
    }

    public AddressLocalData setStartId(int startId)
    {
        this.startId = startId;
        return this;
    }

    public AddressLocalData setEndId(int endId)
    {
        this.endId = endId;
        return this;
    }

    public int getType() {
        return type;
    }

    public String getArea() {
        return area;
    }

    public String getProvince() {
        return province;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getArticleId() {
        return articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getArticleInfo() {
        return articleInfo;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserName() {
        return userName;
    }

    public String getEndProvince() {
        return endProvince;
    }

    public String getEndArea() {
        return endArea;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public String getEndUserName() {
        return endUserName;
    }

    public String getEndPhone() {
        return endPhone;
    }

    public int getEndId() {
        return endId;
    }

    public int getStartId() {
        return startId;
    }
}
