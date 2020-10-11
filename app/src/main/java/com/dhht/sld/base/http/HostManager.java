package com.dhht.sld.base.http;


import com.dhht.config.ServerManager;
import com.dhht.http.request.host.IHost;

/**
 *
 */
public interface HostManager {

    IHost DHHost = new IHost() {

        @Override
        public String getHost() {
            return ServerManager.getInstance().getServer().getMainUrl();
        }

        @Override
        public String getDefaultPath() {
            return "/test/index";
        }
    };
}
