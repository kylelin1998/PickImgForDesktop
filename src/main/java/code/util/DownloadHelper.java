package code.util;

import code.config.RequestProxyConfig;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.util.Timeout;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DownloadHelper {

    private boolean start = false;
    private static Thread monitorThread;
    private static FileOutputStream out;
    private static HttpEntity entity;
    private static Response response;

    public interface DownloadInterface {
        void process(long size, long total);
    }

    public void terminate() throws IOException {
        if (null != out) {
            out.close();
        }
        monitorThread.interrupt();
    }

    public void download(RequestProxyConfig.ProxyConfig proxyConfig, String url, File file, DownloadInterface downloadInterface) throws IOException {
        if (start) {
            return;
        }
        start = true;

        Request request = Request
                .get(url)
                .connectTimeout(Timeout.of(30, TimeUnit.SECONDS));
        RequestProxyConfig.viaProxy(request, proxyConfig);


        response = request.execute();

        response.handleResponse(new HttpClientResponseHandler<Object>() {
            @Override
            public Object handleResponse(ClassicHttpResponse classicHttpResponse) throws HttpException, IOException {
                try {
                    System.out.println(JSON.toJSONString(classicHttpResponse));
                    out = new FileOutputStream(file);
                    entity = classicHttpResponse.getEntity();
                    if (entity != null) {
                        monitorThread = new Thread(() -> {
                            while (true) {
                                downloadInterface.process(file.length(), entity.getContentLength());

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    break;
                                }
                            }
                        });
                        monitorThread.start();

                        entity.writeTo(out);
                        monitorThread.interrupt();
                    }
                } catch (Exception e) {

                } finally {
                    if (null != out) {
                        out.close();
                    }
                    start = false;
                }

                return null;
            }
        });
    }

}
