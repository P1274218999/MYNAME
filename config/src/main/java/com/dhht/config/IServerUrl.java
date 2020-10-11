package com.dhht.config;

public interface IServerUrl {
    String getENV();
    /**
     * 获取前台服务器地址
     * @return
     */
    String getMainUrl();

    /**
     * 获取gis服务器地址
     * @return
     */
    String getGisUrl();

    /**
     * 获取H5静态服务器地址
     * @return
     */
    String getFrontUrl();

    /**
     * 是否可以更改服务器地址
     * @return
     */
    boolean getUpdateConnect();

    /**
     * 是否打印输出日志
     * @return
     */
    boolean getDebugLog();
}
