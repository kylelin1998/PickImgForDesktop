package code.config;

import lombok.Data;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.HttpHost;

public class RequestProxyConfig {

    @Data
    public static class ProxyConfig {
        private Integer type;
        private String hostName;
        private Integer port;
    }

    public static ProxyConfig getProxyConfig(SchemeConfig.SchemeEntity config) {
        if (null == config) {
            return null;
        }
        switch (ProxyTypeEnum.getProxyTypeEnum(config.getProxy().getType())) {
            case NotOpen:
                return null;
            default:
                ProxyConfig proxyConfig = new ProxyConfig();
                proxyConfig.setType(config.getProxy().getType());
                proxyConfig.setHostName(config.getProxy().getHostName());
                proxyConfig.setPort(config.getProxy().getPort());
                return proxyConfig;
        }
    }

    public static void viaProxy(Request request, ProxyConfig proxyConfig) {
        if (null == proxyConfig) {
            return;
        }
        switch (ProxyTypeEnum.getProxyTypeEnum(proxyConfig.getType())) {
            case HttpProxy:
                if (proxyConfig.getType() == 1) {
                    request.viaProxy(new HttpHost(proxyConfig.getHostName(), proxyConfig.getPort()));
                }
                break;
        }
    }

}
