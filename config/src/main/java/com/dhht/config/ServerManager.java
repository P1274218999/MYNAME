package com.dhht.config;

public class ServerManager {

    //当前服务器
    public static final String SERVER = ServerKey.DEV; //切换环境修改此处

    //单例实例
    public static ServerManager mInstance;

    //服务器地址接口
    private IServerUrl mServer;

    //环境关键词
    public static class ServerKey{
        /**
         * 开发环境
         */
        public static final String DEV = "dev";
        /**
         * 测试环境
         */
        public static final String TEST = "test";
        /**
         * 正式环境
         */
        public static final String PRO = "pro";
    }

    /**
     * 获取单例实例
     * @return
     */
    public static ServerManager getInstance(){
        if (mInstance == null) {
            synchronized (ServerManager.class) {
                if (mInstance == null) {
                    mInstance = new ServerManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取环境
     * @return
     */
    public IServerUrl getServer(){
        if (mServer == null) {
            synchronized (this) {
                if (mServer == null) {
                    if(SERVER.equals(ServerKey.DEV)){
                        mServer = new DevServer();
                    }else if(SERVER.equals(ServerKey.PRO)){
                        mServer = new ProServer();
                    }else{
                        //默认开发环境
                        mServer = new DevServer();
                    }
                }
            }
        }
        return mServer;
    }
}
