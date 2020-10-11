package com.dhht.config;

public class DevServer implements IServerUrl {
    /**
     * 开发环境
     *
     * @return
     */
    @Override
    public String getENV() {
        return "dev";
    }

    /**
     * 获取前台服务器地址
     *
     * @return
     */
    @Override
    public String getMainUrl() {
        return "http://sld.dhht.haiyunyi.cn";
    }

    /**
     * 获取gis服务器地址
     *
     * @return
     */
    @Override
    public String getGisUrl() {
        return null;
    }

    /**
     * 获取H5静态服务器地址
     *
     * @return
     */
    @Override
    public String getFrontUrl() {
        return null;
    }

    /**
     * 是否可以更改服务器地址
     *
     * @return
     */
    @Override
    public boolean getUpdateConnect() {
        return false;
    }

    /**
     * 是否打印输出日志
     *
     * @return
     */
    @Override
    public boolean getDebugLog() {
        return false;
    }
}
