package ZLYUtils;


import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息头类型
 */
public class NetworkHeaders {
    private Map<String,List<String>> headers;

    public long getResponseTime() {
        return responseTime;
    }

    public HttpRequestBase getHttpRequestBase() {
        return httpRequestBase;
    }
    void setHttpRequestBase(HttpRequestBase httpRequestBase) {
        this.httpRequestBase = httpRequestBase;
    }

    private HttpRequestBase httpRequestBase;
    void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    private long responseTime;
    public int getResponseCode() {
        return responseCode;
    }

    void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    private int responseCode;

    public NetworkHeaders(){
        this.headers = new HashMap<>();
        this.responseCode = -1;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }


}
