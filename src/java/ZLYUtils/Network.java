package ZLYUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Network {
    public static String networkUrl;
    public static String sendGet(String url,String param,Map<String,String> headers) {
        if (url == null) throw new IllegalArgumentException("url参数为空");
        return sendGet(url, "",headers,null);
    }

    /**
     * 发送get请求
     *
     * @param url
     * @param param
     * @return 服务响应内容, null为响应非200
     */
    public static String sendGet(String url, String param,Map<String,String> headers,
                                 NetworkHeaders networkHeaders) {
        LocalProxy();
        if (url == null) throw new IllegalArgumentException("url参数为空");
        if (param == null) throw new IllegalArgumentException("param参数为空");
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
            if(headers!=null && headers.size()!=0){
                for(Map.Entry<String,String> entry : headers.entrySet()){
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            // 不能使用缓存
            connection.setUseCaches(false);
            // 建立实际的连接
            connection.connect();
            if(networkHeaders!=null)  networkHeaders.setHeaders(connection.getHeaderFields());

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


    public static String sendPost(String url, String param,Map<String,String> headers) {
        return sendPost(url,param,headers,null);
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
    public static String sendPost(String url, String param,Map<String,String> headers,
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
            if(headers!=null && headers.size()!=0){
                for(Map.Entry<String,String> entry : headers.entrySet()){
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
            if(networkHeaders!=null)  networkHeaders.setHeaders(conn.getHeaderFields());
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
            if(str.indexOf("%",str.length()-1)!=-1)str = str.substring(0,str.length()-1);
            result = java.net.URLDecoder.decode(str, encodingName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
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
}
