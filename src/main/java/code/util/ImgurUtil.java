package code.util;

import code.config.RequestProxyConfig;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ImgurUtil {

    @Data
    public static class UploadResponse {
        private boolean ok;
        private int status;
        private UploadData data;
    }
    @Data
    public static class UploadData {
        private String id;
        private String deleteHash;
        private String link;
    }

    private static Form convertFrom(Map<String, String> parameters) {
        Form form = Form.form();
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            form.add(param.getKey(), param.getValue());
        }
        return form;
    }


    public static UploadResponse upload(RequestProxyConfig.ProxyConfig proxyConfig, String clientId, String filename, String base64) {
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("image", base64);
            parameters.put("type", "base64");
            parameters.put("name", filename);

            Request request = Request
                    .post("https://api.imgur.com/3/image")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .addHeader("Authorization", "Client-ID " + clientId)
                    .bodyForm(convertFrom(parameters).build(), Charset.forName("UTF-8"));
            RequestProxyConfig.viaProxy(request, proxyConfig);

            Response execute = request.execute();
            String s = execute.returnContent().asString(Charset.forName("UTF-8"));

            log.info("upload image by Imgur, response: {}", s);

            UploadResponse response = JSON.parseObject(s, UploadResponse.class, JSONReader.Feature.SupportSmartMatch);
            response.setOk(response.status == 200);
            return response;
        } catch (Exception e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
        UploadResponse response = new UploadResponse();
        response.setOk(false);
        return response;
    }

}
