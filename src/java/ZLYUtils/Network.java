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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;


public class Network {
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
        LocalProxy();
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


    public static String sendGet(String url, String param, Map<String, String> headers) {
        if (url == null) throw new IllegalArgumentException("url参数为空");
        return sendGet(url, "", headers, null);
    }

    public static String doGetJson(String url, String param, Map<String, String> headers,
                                   NetworkHeaders networkHeaders) {
        LocalProxy();
        if (url == null) throw new IllegalArgumentException("url参数为空");
        String result = "";
        CloseableHttpClient httpClient = null;
        try {
            // 1. 创建HttpClient对象
            httpClient = getHttpClient();
            // 2. 创建HttpGet对象
            HttpGet httpGet = new HttpGet(getURI(url, param));
            httpGet.setConfig(requestConfig());
            httpGet.addHeader("Accept-Encoding", "chunked");
            httpGet.addHeader("Charset", CHARSET_UTF_8);
            if (null != headers) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }

//            // 3. 执行GET请求
//            response = httpClient.execute(httpGet);
//            // 4. 获取响应实体
//            HttpEntity entity = response.getEntity();
            // 5. 处理响应实体
            result = getResponse(httpClient.execute(httpGet), networkHeaders);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6. 释放资源
            //不可以关闭，不然连接池就会被关闭
            //httpclient.close();
        }
        return result;
    }

    public static URI getURI(String url, String param) {
        URI uri = null;
        try {
            if (param == null || param.equals("")) {
                uri = new URIBuilder(url).build();
            } else {
                uri = new URIBuilder(url + "?" + param).build();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }


    public static String doPostJson(String url, String param,
                                    Map<String, String> requestHead,
                                    NetworkHeaders networkHeaders) {
        LocalProxy();
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
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

    public static String doGet(String url
            , Map<String, String> param
            , Map<String, String> requestHead
            , NetworkHeaders networkHeaders) {

        String result = "";
        CloseableHttpClient httpclient = null;
        try {
            String params = toHttpGetParams(param);
            httpclient = getHttpClient();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(getURI(url, params));
            httpGet.setConfig(requestConfig());
            httpGet.addHeader("Accept-Encoding", "chunked");
            httpGet.addHeader("Charset", CHARSET_UTF_8);

            if (null != requestHead) {
                for (Map.Entry<String, String> entry : requestHead.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    httpGet.addHeader(key, value);
                }
            }
            LocalProxy();
            result = getResponse(httpclient.execute(httpGet), networkHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }

    public static String doGet(String url, Map<String, String> param) {
        return doGet(url, param, null, null);
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

    public static String doPost(String url) {
        return doPost(url, null);
    }


    /**
     * 发送get请求
     *
     * @param url
     * @param param
     * @return 服务响应内容, null为响应非200
     */
    public static String sendGet(String url, String param, Map<String, String> headers,
                                 NetworkHeaders networkHeaders) {
        LocalProxy();
        if (url == null) throw new IllegalArgumentException("url参数为空");
        StringBuffer data = new StringBuffer();
        ;
        BufferedReader in = null;
        String urlStr = url + "?" + param;
        if (param.equals("")) urlStr = url;
        try {
            networkUrl = url;
            URL realUrl = new URL(urlStr);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Accept-Encoding", "chunked");
            connection.setRequestProperty("Charset", "utf-8");
            if (headers != null && headers.size() != 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            // 不能使用缓存
            connection.setUseCaches(false);
            // 建立实际的连接
            connection.connect();
            if (networkHeaders != null) networkHeaders.setHeaders(connection.getHeaderFields());
            int getResponseCode = ((HttpURLConnection) connection).getResponseCode();
            if (getResponseCode != 200) {// 检查服务器响应
                return getResponseCode + "";
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                data.append(line);
            }

        } catch (Exception e) {
            data.append("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return data.toString();
    }


    public static String sendPost(String url, String param, Map<String, String> headers) {
        return sendPost(url, param, headers, null);
    }


    /**
     * 向指定 URL 发送POST方法的请求
     * 本方法暂不支持GZIP解压，所以没有设置Accept-Encoding
     * 默认为创新版
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     * @author ZhangLianYu
     */
    public static String sendPost(String url, String param, Map<String, String> headers,
                                  NetworkHeaders networkHeaders) {
        LocalProxy();
        if (url == null) throw new IllegalArgumentException("url参数为空");
        if (param == null) throw new IllegalArgumentException("param参数为空");
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        networkUrl = url;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            ((HttpURLConnection) conn).setInstanceFollowRedirects(false);// 302重定向
            conn.setRequestProperty("Content-Length", String.valueOf(param.length()));
            conn.setRequestProperty("Accept-Encoding", "chunked");
            conn.setRequestProperty("Charset", "utf-8");
            if (headers != null && headers.size() != 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            //      conn.setRequestProperty("Cookie", "Hm_lvt_a1e24e560b068c813bc6544a78bb2892=1531294797,1531361048; wwa=puq7a30kQSa6xGga6OVw+w==; JSESSIONID=2949BABDF401BB459A69D16229711F6A");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 建立实际的连接
            conn.connect();
//            Map<String, List<String>> map = conn.getHeaderFields();
////            遍历所有的响应头字段
//            System.out.println(map.get("Charset"));
            if (networkHeaders != null) networkHeaders.setHeaders(conn.getHeaderFields());
            int getResponseCode = ((HttpURLConnection) conn).getResponseCode();
            if (getResponseCode != 200) {// 检查服务器响应
                return getResponseCode + "";
            }
//            if (getResponseCode == 302) {
//                // 如果会重定向，保存302重定向地址，以及Cookies,然后重新发送请求(模拟请求)
//                String location = conn.getHeaderField("Location");
//                //StringText cookies = conn.getHeaderField("Set-Cookie");
//                if ("/".equals(location)) {
//                    location = "http://jira.ffan.biz" + location;
//                }
//                realUrl = new URL(location);
//                conn = (HttpURLConnection) realUrl.openConnection();
//                conn.addRequestProperty("Accept-Charset", "UTF-8;");
//                conn.setRequestProperty("accept", "*/*");
//                conn.addRequestProperty("User-Agent", userAgent);
//                conn.setRequestProperty("Accept-Encoding", "chunked");
//                conn.connect();
//                System.out.println("跳转地址:" + location);
//                map = conn.getHeaderFields();
//                //	setCookies(map);
//            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            result.append("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //用新的字符编码生成字符串
        return result.toString();

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
        System.out.println(doGet("http://www.baidu.com", null, null, null));
    }


}
