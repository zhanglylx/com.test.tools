package ZLYUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http类型，用于发送网络请求
 */
public class HttpUtils {
    // HTTP内容类型。相当于form表单的形式，提交数据
    private static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";
    // utf-8字符编码
    public static final String CHARSET_UTF_8 = "UTF-8";
    // 超时时间:ms
    private static final int SOCKET_TIME_OUT = 60 * 1000;
    //创建连接的最长时间:ms
    private static final int CONNECTION_TIME_OUT = 60 * 2000;
    // 从连接池中获取到连接的最长时间:ms
    private static final int CONNECTION_REQUEST_TIME_OUT = 60 * 2000;
    // 线程池最大连接数
    private static final int POOL_MAX_TOTAL = 3 * 1000;
    //默认的每个路由的最大连接数
    private static final int POOL_MAX_PERROUTE = 10;
    //检查永久链接的可用性:ms
    private static final int POOL_VALIDATE_AFTER_INACTIVITY = 2 * 1000;
    //关闭Socket等待时间，单位:s
    private static final int SOCKET_LINGER = 60;
    //建立httpClient配置
    private static HttpClientBuilder httpBulder;

    static {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            // 配置同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create
                    ().register(
                    "http", PlainConnectionSocketFactory.getSocketFactory()).register(
                    "https", sslsf).build();
            // 初始化连接管理器
            // 连接管理器
            PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);
            // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
            pool.setMaxTotal(POOL_MAX_TOTAL);
            //默认的每个路由的最大连接数
            pool.setDefaultMaxPerRoute(POOL_MAX_PERROUTE);
            //官方推荐使用这个来检查永久链接的可用性，而不推荐每次请求的时候才去检查
            pool.setValidateAfterInactivity(POOL_VALIDATE_AFTER_INACTIVITY);
            //设置默认连接配置
            pool.setDefaultSocketConfig(setSocketConfig());
            //请求重试处理
            // 如果已经重试了3次，就放弃
            // 如果服务器丢掉了连接，那么就重试
            // 不要重试SSL握手异常
            // 超时
            // 目标服务器不可达
            // ssl握手异常
            // 如果请求是幂等的，就再次尝试
            //请求重试处理
            HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
                if (executionCount >= 3) return false;// 如果已经重试了3次，就放弃
                if (exception instanceof NoHttpResponseException) return true; // 如果服务器丢掉了连接，那么就重试
                if (exception instanceof SSLHandshakeException) return false; // 不要重试SSL握手异常
                if (exception instanceof InterruptedIOException) return false; // 超时
                if (exception instanceof UnknownHostException) return false;// 目标服务器不可达
                if (exception instanceof SSLException) return false;// ssl握手异常
                // 如果请求是幂等的，就再次尝试
                return !(HttpClientContext.adapt(context).getRequest()
                        instanceof HttpEntityEnclosingRequest);
            };
            httpBulder = HttpClients.custom()
                    .setConnectionManager(pool) // 设置请求配置
                    .setDefaultRequestConfig(requestConfig())
//                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                    .setRetryHandler(httpRequestRetryHandler);  // 设置重试次数

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * socket配置（默认配置 和 某个host的配置）
     */
    private static SocketConfig setSocketConfig() {
        return SocketConfig.custom()
                .setTcpNoDelay(true)     //是否立即发送数据，设置为true会关闭Socket缓冲，默认为false
                .setSoReuseAddress(true) //是否可以在一个进程关闭Socket后，即使它还没有释放端口，其它进程还可以立即重用端口
                .setSoTimeout(SOCKET_TIME_OUT)       //接收数据的等待超时时间，单位ms
                .setSoLinger(SOCKET_LINGER)         //关闭Socket时，要么发送完所有数据，要么等待XXs后，就关闭连接，此时socket.close()是阻塞的
                .setSoKeepAlive(true)    //开启监视TCP连接是否有效
                .build();
    }

    /**
     * 构建请求配置信息
     * 超时时间
     */
    private static RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIME_OUT) // 创建连接的最长时间
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(SOCKET_TIME_OUT) // 数据传输的最长时间
                .setProxy(new HttpHost("localhost", 8888))
                .build();
    }

    private static CloseableHttpClient getHttpClient() {
        return httpBulder.build();
    }

    public static String doGet(String url, String param, Map<String, String> headers) {
        return doGet(getURI(url, param), headers, null);
    }

    public static String doGet(String url, String param) {
        return doGet(getURI(url, param));
    }

    public static String doGet(URI uri) {
        return doGet(uri, null, null);
    }

    public static String doGet(URI uri, Map<String, String> headers
            , NetworkHeaders networkHeaders) {
        String result = "";
        try {
            result = getResponse(getHttpGet(uri, headers), networkHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //不可以关闭，不然连接池就会被关闭
//            httpClient.close();
        }
        return result;
    }

    public static String doPostJson(String url, String param) {
        return doPostJson(url, param, null, null);
    }


    public static String doPost(String url, Object param) {
        return doPost(url, param, null, null);
    }

    public static String doPost(String url, Map<String, String> param) {
        return doPost(url, param, null, null);
    }

    @SuppressWarnings("unchecked")
    public static String doPost(String url
            , Object param
            , Map<String, String> requestHead
            , NetworkHeaders networkHeaders) {
        String resultString = "";

        try {
            HttpPost httpPost = getHttpPost(url, requestHead);
            // 创建参数列表
            if (param != null) {
                if (param instanceof Map) {
                    Map<String, String> mapParam = (Map<String, String>) param;
                    List<NameValuePair> paramList = new ArrayList<>();
                    for (String key : mapParam.keySet()) {
                        paramList.add(new BasicNameValuePair(key, mapParam.get(key)));
                    }
                    // 模拟表单
                    httpPost.setEntity(new UrlEncodedFormEntity(paramList));
                } else if (param instanceof String) {
                    StringEntity entity = new StringEntity((String) param);
                    httpPost.setEntity(entity);
                }
            }
            resultString = getResponse(httpPost, networkHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static String doPostJson(String url
            , String param
            , Map<String, String> requestHead
            , NetworkHeaders networkHeaders) {
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = getHttpPost(url, requestHead);
            // 创建请求内容
            if (null == param) param = "";
            StringEntity entity = new StringEntity(param, ContentType.APPLICATION_JSON);
            entity.setContentType(CONTENT_TYPE_JSON_URL);
            httpPost.setEntity(entity);
            // 执行http请求
            resultString = getResponse(httpPost, networkHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private static Header[] getHeaders(Map<String, String> requestHead) {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Accept-Encoding", "chunked"));
        headers.add(new BasicHeader("Charset", CHARSET_UTF_8));
        if (null != requestHead) {
            for (Map.Entry<String, String> entry : requestHead.entrySet()) {
                headers.add(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }

        return headers.toArray(new Header[]{});
    }

    private static HttpGet getHttpGet(URI uri, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeaders(getHeaders(headers));
        return httpGet;
    }

    private static HttpPost getHttpPost(String url, Map<String, String> requestHead) {
        // 创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeaders(getHeaders(requestHead));
        return httpPost;
    }

    /**
     * 获取响应正文
     *
     * @param httpRequestBase
     * @param networkHeaders
     * @return
     */
    private static String getResponse(HttpRequestBase httpRequestBase
            , NetworkHeaders networkHeaders) {
        String result = "";
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            HttpClient httpClient;
            CloseableHttpClient closeableHttpClient = getHttpClient();
            long startTime = System.currentTimeMillis();
            closeableHttpResponse = closeableHttpClient.execute(httpRequestBase);
            if (networkHeaders != null){
                networkHeaders.setResponseTime(System.currentTimeMillis() - startTime);
                networkHeaders.setHttpRequestBase(httpRequestBase);
            }
            if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
                result = closeableHttpResponse.getStatusLine().getStatusCode() + "";
            } else {
                responseNetworkHeaders(closeableHttpResponse, networkHeaders);
                result = EntityUtils.toString(closeableHttpResponse.getEntity(), CHARSET_UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (closeableHttpResponse != null) closeableHttpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getURLDecoderString(result, CHARSET_UTF_8);

    }


    /**
     * 将服务器响应头添加到networkHeaders
     *
     * @param closeableHttpResponse
     * @param networkHeaders
     */
    private static void responseNetworkHeaders(CloseableHttpResponse closeableHttpResponse
            , NetworkHeaders networkHeaders) {
        if (networkHeaders != null) {
            networkHeaders.setResponseCode(closeableHttpResponse.getStatusLine().getStatusCode());
            Map<String, List<String>> map = new HashMap<>();
            List<String> list;
            for (Header header : closeableHttpResponse.getAllHeaders()) {
                list = new ArrayList<>();
                for (HeaderElement element : header.getElements()) {
                    list.add(element.getName());
                }
                map.put(header.getName(), list);
            }
            networkHeaders.setHeaders(map);
        }

    }

    public static URI getURI(String url, String param) {
        URI uri = null;
        try {
            if (param == null || param.equals("")) {
                uri = new URIBuilder(url.trim()).build();
            } else {
                uri = new URIBuilder(url.trim()).setCustomQuery(param.trim()).build();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }


    /**
     * 设置本地代理
     */
    public static void LocalProxy() {
        System.setProperty("http.proxySet", "true"); //将请求通过本地发送
        System.setProperty("http.proxyHost", "127.0.0.1");  //将请求通过本地发送
        System.setProperty("http.proxyPort", "8888"); //将请求通过本地发送
    }

    /**
     * 转码
     *
     * @param param
     * @return
     */
    public static String getEncoderString(String param, String encoder) {
        param = param.replace("\n", "\r\n");
        try {
            param = URLEncoder.encode(param, encoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return param;
    }


    /**
     * URL 解码
     *
     * @return StringText
     * @author zhanglianyu
     * @date 2017.7.23
     */
    public static String getURLDecoderString(String str, String encodingName) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            str = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            str = str.replaceAll("\\+", "%2B");
            if (str.indexOf("%", str.length() - 1) != -1) str = str.substring(0, str.length() - 1);
            result = URLDecoder.decode(str, encodingName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 这里只是其中的一种场景,也就是把参数用&符号进行连接且进行URL编码
     * 根据实际情况拼接参数
     */
    private static String toHttpGetParams(Map<String, String> param) throws Exception {
        String res = "";
        if (param == null) {
            return res;
        }
        for (Map.Entry<String, String> entry : param.entrySet()) {
            res += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), CHARSET_UTF_8) + "&";
        }
        return "".equals(res) ? "" : StringUtils.chop(res);
    }


    //判断是否为16进制数
    private static boolean isHex(char c) {
        if (((c >= '0') && (c <= '9')) ||
                ((c >= 'a') && (c <= 'f')) ||
                ((c >= 'A') && (c <= 'F')))
            return true;
        else
            return false;
    }

    private static String convertPercent(String str) {
        StringBuilder sb = new StringBuilder(str);

        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            //判断是否为转码符号%
            if (c == '%') {
                if (((i + 1) < sb.length() - 1) && ((i + 2) < sb.length() - 1)) {
                    char first = sb.charAt(i + 1);
                    char second = sb.charAt(i + 2);
                    //如只是普通的%则转为%25
                    if (!(isHex(first) && isHex(second)))
                        sb.insert(i + 1, "25");
                } else {//如只是普通的%则转为%25
                    sb.insert(i + 1, "25");
                }

            }
        }

        return sb.toString();
    }


    public static void main(String[] args) {
//        Map<String, String> map = new HashMap<>();
//        map.put("1", "ddddd=");
//        System.out.println(doPost("http://www.baidu.com", map));
//        URIBuilder uriBuilder = new URIBuilder();
//        uriBuilder.setPath("http://www.baidu.com");
//        uriBuilder.setHost("33");
//        uriBuilder.setScheme("3444");
//        uriBuilder.addParameter("3", "33");
//        uriBuilder.addParameter("3", "33");
//        uriBuilder.setCustomQuery("a=搜索");
        System.out.println(doPost("http://www.baidu.com", "", null, null));

    }


}
