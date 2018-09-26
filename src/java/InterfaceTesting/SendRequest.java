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
    private String matchingRule;//匹配规则
    private String transcoding;//转码文本
    private String beginTranscoding;//转码文本开始正则
    private String endTranscoding;//转码文本开始正则
    private String url;

    public SendRequest() {
    }


    /**
     * 发送请求
     *
     * @return
     */
    public String sendRequest() {
        if (this.transcoding != null && !"".equals(this.transcoding)) {
            String encoderStr = beginTranscoding + transcoding + endTranscoding;//转码前
            if(!urlValues.contains(encoderStr) && !body.contains(encoderStr))return "转码文本未找到:"+transcoding;
            //转码后
            String encoderText = beginTranscoding +
                    Network.getEncoderString(transcoding, "UTF-8") + endTranscoding;
            urlValues = urlValues.replace(encoderStr, encoderText);
            body = body.replace(encoderStr, encoderText);
        }
        if ((agreementValues == null || "".equals(agreementValues)) &&
                (method == null || "".equals(method)) &&
                (path == null || "".equals(path))
                ) return "";
        //给url赋值
        if (!"".equals(urlValues)) {
            this.url = agreementValues + "://" + path + "?" + urlValues;
        } else {
            this.url = agreementValues + "://" + path;
        }
        if (InterfaceConfig.URL_GET_NAME.equals(this.method)) {
            return (Network.sendGet(agreementValues + "://" + path, urlValues,null));
        } else if (InterfaceConfig.URL_POST_NAME.equals(this.method)) {
            if ("".equals(urlValues)) {
                return (Network.sendPost(agreementValues + "://" + path, body,null));
            } else {
                return (Network.sendPost(agreementValues + "://" + path + "?" + urlValues, body,null));
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

    public String getMatchingRule() {
        return matchingRule;
    }

    public void setMatchingRule(String matchingRule) {
        this.matchingRule = matchingRule;
    }

    public String getTranscoding() {
        return transcoding;
    }

    public void setTranscoding(String transcoding) {
        this.transcoding = transcoding;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBeginTranscoding() {
        return beginTranscoding;
    }

    public void setBeginTranscoding(String beginTranscoding) {
        this.beginTranscoding = beginTranscoding;
    }

    public String getEndTranscoding() {
        return endTranscoding;
    }

    public void setEndTranscoding(String endTranscoding) {
        this.endTranscoding = endTranscoding;
    }
}
