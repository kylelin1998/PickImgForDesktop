package code.util;

import code.config.RequestProxyConfig;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.util.Timeout;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GithubUtil {

    private final static String ApiVersion = "2022-11-28";

    @Data
    public static class UploadParameters {
        private String owner;
        private String repo;
        private String path;
        private String message;
        private String content;
        private String branch;
    }
    @Data
    public static class UploadResponse {
        private boolean ok;
        private Integer statusCode;
    }
    @Data
    public static class LatestReleaseResponse {
        private boolean ok;
        private String htmlUrl;
        private String tagName;
        private String name;
        private String body;
        private List<LatestReleaseAsset> assets;
    }
    @Data
    public static class LatestReleaseAsset {
        private String name;
        private String browserDownloadUrl;
    }

    public static UploadResponse upload(RequestProxyConfig.ProxyConfig proxyConfig, String token, UploadParameters parameters) {
        String url = String.format("https://api.github.com%s", String.format("/repos/%s/%s/contents%s", parameters.getOwner(), parameters.getRepo(), parameters.getPath()));
        String body = JSON.toJSONString(parameters);
        UploadResponse githubUploadResponse = new UploadResponse();
        githubUploadResponse.setOk(false);
        try {
            Request request = Request
                    .put(url)
                    .bodyString(body, ContentType.APPLICATION_JSON)
                    .setHeader("Accept", "application/vnd.github+json")
                    .setHeader("X-GitHub-Api-Version", ApiVersion)
                    .setHeader("Authorization", String.format("Bearer %s", token))
                    .connectTimeout(Timeout.of(60, TimeUnit.SECONDS))
                    .responseTimeout(Timeout.of(360, TimeUnit.SECONDS));
            RequestProxyConfig.viaProxy(request, proxyConfig);
            HttpResponse httpResponse = request.execute().returnResponse();

            githubUploadResponse.setOk(httpResponse.getCode() == 201);
            githubUploadResponse.setStatusCode(httpResponse.getCode());
            return githubUploadResponse;
        } catch (Exception e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
        return githubUploadResponse;
    }

    public static LatestReleaseResponse getLatestRelease(RequestProxyConfig.ProxyConfig proxyConfig, String owner, String repo) {
        String url = String.format("https://api.github.com/repos/%s/%s/releases/latest", owner, repo);
        try {
            Request request = Request
                    .get(url)
                    .setHeader("Accept", "application/vnd.github+json")
                    .setHeader("X-GitHub-Api-Version", ApiVersion)
                    .connectTimeout(Timeout.of(15, TimeUnit.SECONDS))
                    .responseTimeout(Timeout.of(60, TimeUnit.SECONDS));
            RequestProxyConfig.viaProxy(request, proxyConfig);
            Response execute = request.execute();

            LatestReleaseResponse releaseAssetResponse = JSON.parseObject(execute.returnContent().asString(Charset.forName("UTF-8")), LatestReleaseResponse.class, JSONReader.Feature.SupportSmartMatch);
            releaseAssetResponse.setOk(true);
            return releaseAssetResponse;
        } catch (Exception e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
        LatestReleaseResponse response = new LatestReleaseResponse();
        response.setOk(false);
        return response;
    }

}
