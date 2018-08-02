package InterfaceTesting;

import ZLYUtils.Network;

/**
 * 发送网路请求
 */
public class SendRequest {
    private String agreementValues;//记录协议
    private String method;//方法
    private String path;//路径
    private String urlValues;//url中的参数
    private String body;//body

    public SendRequest() {
    }



    /**
     * 发送请求
     *
     * @return
     */
    public String sendRequest() {
        if (InterfaceConfig.URL_GET_NAME.equals(this.method)) {
            return (Network.sendGet(agreementValues + "://" + path, urlValues));
        } else if (InterfaceConfig.URL_POST_NAME.equals(this.method)) {
            if ("".equals(urlValues)) {
                return (Network.sendPost(agreementValues + "://" + path, body));
            } else {
                return (Network.sendPost(agreementValues + "://" + path + "?" + urlValues, body));
            }

        }
        return "null";
    }


    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(agreementValues);
        stringBuffer.append("://");
        stringBuffer.append(path);
        if (urlValues.length() > 0) {
            stringBuffer.append("?");
            stringBuffer.append(urlValues);
        }
        return stringBuffer.toString();
    }


    public String getAgreementValues() {
        return agreementValues;
    }

    public void setAgreementValues(String agreementValues) {
        this.agreementValues = agreementValues;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrlValues() {
        return urlValues;
    }

    public void setUrlValues(String urlValues) {
        this.urlValues = urlValues;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
