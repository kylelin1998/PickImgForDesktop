package code.util;

import code.config.ProxyTypeEnum;
import code.config.RequestProxyConfig;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Slf4j
public class AlibabaCloudOSSUtil {

    @Data
    public static class UploadParameters {
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String bucketName;
        private String objectName;
        private byte[] content;
    }
    @Data
    public static class UploadResponse {
        private boolean ok;
        private String errorMessage;
    }

    public static UploadResponse upload(RequestProxyConfig.ProxyConfig proxyConfig, UploadParameters parameters) {
        ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
        if (null != proxyConfig) {
            switch (ProxyTypeEnum.getProxyTypeEnum(proxyConfig.getType())) {
                case HttpProxy:
                    if (proxyConfig.getType() == 1) {
                        configuration.setProxyHost(proxyConfig.getHostName());
                        configuration.setProxyPort(proxyConfig.getPort());
                    }
                    break;
            }
        }
        UploadResponse response = new UploadResponse();
        response.setOk(false);

        OSS ossClient = new OSSClientBuilder().build(parameters.getEndpoint(), parameters.getAccessKeyId(), parameters.getAccessKeySecret(), configuration);
        try {
            ossClient.putObject(parameters.getBucketName(), parameters.getObjectName(), new ByteArrayInputStream(parameters.getContent()));
            response.setOk(true);
            return response;
        } catch (Exception e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
            response.setErrorMessage(e.getMessage());
        } finally {
            if (null != ossClient) {
                ossClient.shutdown();
            }
        }
        return response;
    }

}
