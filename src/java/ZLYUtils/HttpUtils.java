package ZLYUtils;

import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

/**
 * http类型，用于发送网络请求
 */
public class HttpUtils {
    public static String networkUrl;
    // HTTP内容类型。相当于form表单的形式，提交数据
    public static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";

    // utf-8字符编码
    public static final String CHARSET_UTF_8 = "utf-8";

    // 连接管理器
    private static PoolingHttpClientConnectionManager pool;

    // 请求配置
    private static RequestConfig requestConfig;

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
            pool = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);
            // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
            pool.setMaxTotal(200);
            // 设置最大路由
            pool.setDefaultMaxPerRoute(2);

            requestConfig = requestConfig();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建请求配置信息
     * 超时时间什么的
     */
    private static RequestConfig requestConfig() {
        // 根据默认超时限制初始化requestConfig
        int socketTimeout = 10000;
        int connectTimeout = 10000;
        int connectionRequestTimeout = 10000;
        HttpHost httpHost = new HttpHost("127.0.0.1", 8888);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeout) // 创建连接的最长时间
                .setConnectionRequestTimeout(connectionRequestTimeout) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(socketTimeout) // 数据传输的最长时间
                .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
                .setProxy(httpHost)
                .build();

        return config;
    }

    public static CloseableHttpClient getHttpClient() {

        CloseableHttpClient httpClient = HttpClients.custom()
                // 设置连接池管理
                .setConnectionManager(pool)
                // 设置请求配置
                .setDefaultRequestConfig(requestConfig)
                // 设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .build();

        return httpClient;
    }

    public static String doGet(URI uri) {
        return doGet(uri, null, null);
    }

    public static String doGet(URI uri, Map<String, String> headers,
                               NetworkHeaders networkHeaders) {
        String result = "";
        CloseableHttpClient httpClient = null;
        try {
            // 1. 创建HttpClient对象
            httpClient = getHttpClient();
            // 2. 创建HttpGet对象
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig());
            httpGet.addHeader("Accept-Encoding", "chunked");
            httpGet.addHeader("Charset", CHARSET_UTF_8);
            if (null != headers) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            result = getResponse(httpClient.execute(httpGet), networkHeaders);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //不可以关闭，不然连接池就会被关闭
            //httpclient.close();
        }
        return result;
    }

    private static URI getURI(String url, String param) {
        URI uri = null;

        try {
            if (param == null || param.equals("")) {
                uri = new URIBuilder(url).build();
            } else {
                uri = new URIBuilder().setPath(url).setCustomQuery(param).build();

            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }


    /**
     * 获取响应正文
     *
     * @param closeableHttpResponse
     * @param networkHeaders
     * @return
     */
    private static String getResponse(CloseableHttpResponse closeableHttpResponse
            , NetworkHeaders networkHeaders
    ) {
        String result = "";
        try {
            if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
                result = closeableHttpResponse.getStatusLine().getStatusCode() + "";
            } else {
                if (closeableHttpResponse.getEntity() != null) {
                    responseNetworkHeaders(closeableHttpResponse, networkHeaders);
                    result = EntityUtils.toString(closeableHttpResponse.getEntity(), CHARSET_UTF_8);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                closeableHttpResponse.close();
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
    private static void responseNetworkHeaders(CloseableHttpResponse closeableHttpResponse, NetworkHeaders networkHeaders) {
        if (networkHeaders != null) {
            Map<String, List<String>> map = new HashMap<>();
            List<String> list;
            for (Header header : closeableHttpResponse.getAllHeaders()) {
                list = new ArrayList<>();
                for (HeaderElement element : header.getElements()) {
                    list.add(element.getValue());
                }
                map.put(header.getName(), list);
            }
            networkHeaders.setHeaders(map);
        }

    }

    public static String doPostJson(String url, String param) {
        return doPostJson(url, param, null, null);
    }

    /**
     * @param url
     * @param param
     * @param requestHead
     * @param networkHeaders
     * @return
     */
    public static String doPostJson(String url, String param,
                                    Map<String, String> requestHead,
                                    NetworkHeaders networkHeaders) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            if (null == param) param = "";
            StringEntity entity = new StringEntity(param, ContentType.APPLICATION_JSON);
            entity.setContentType(CONTENT_TYPE_JSON_URL);
            httpPost.setEntity(entity);
            httpPost.setConfig(requestConfig());
            httpPost.addHeader("Accept-Encoding", "chunked");
            httpPost.addHeader("Charset", CHARSET_UTF_8);
            if (null != requestHead) {
                for (Map.Entry<String, String> entry : requestHead.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 执行http请求
            resultString = getResponse(httpClient.execute(httpPost), networkHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }


    public static String doPost(String url
            , Map<String, String> param
            , Map<String, String> requestHead
            , NetworkHeaders networkHeaders) {
        LocalProxy();
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig());
            httpPost.addHeader("Accept-Encoding", "chunked");
            httpPost.addHeader("Charset", CHARSET_UTF_8);
            if (null != requestHead) {
                for (Map.Entry<String, String> entry : requestHead.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            resultString = getResponse(httpClient.execute(httpPost), networkHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doPost(String url, Map<String, String> param) {
        return doPost(url, param, null, null);
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
            param = java.net.URLEncoder.encode(param, encoder);
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
            result = java.net.URLDecoder.decode(str, encodingName);
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
        Map<String,String> map = new HashMap<>();
        map.put("1","2");
        System.out.println(doPost("http://www.baidu.com", map));

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath("http://www.baidu.com");
        uriBuilder.setHost("33");
        uriBuilder.setScheme("3444");
        uriBuilder.addParameter("3", "33");
        uriBuilder.addParameter("3", "33");
        uriBuilder.setCustomQuery("a=搜索");
        System.out.println(uriBuilder);
    }


}
