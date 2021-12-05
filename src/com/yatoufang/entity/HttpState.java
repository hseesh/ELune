package com.yatoufang.entity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yatoufang.service.PersistentService;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.Arrays;
import java.util.List;

/**
 * @author hse
 * @Date: 2021/3/4 0004
 */
public class HttpState {
    private String description;
    private String bodyType;
    private String Url;
    private String shortUrl;
    private String method;
    private Object[][] file;
    private Object[][] heads;
    private Object[][] params;
    private List<MyCookie> requestCookies;

    private int responseCode;
    private Long responseTime;
    private long contentLength;
    private String responseBody;
    private Header[] requestHeads;
    private Header[] responseHeads;
    private List<Cookie> responseCookies;

    public HttpState() {
        this.Url = "";
        this.shortUrl = "";
        this.method = "GET";
    }

    public void loadLocalData() {
        String hostInfo = PersistentService.readYaml(null, true);
        if (hostInfo != null) {
            setUrl(hostInfo + getShortUrl());
        } else {
            hostInfo = PersistentService.getHostInfo();
            if (hostInfo != null) {
                setUrl(hostInfo + getShortUrl());
            } else {
                setUrl(getShortUrl());
            }
        }

        String headers = PersistentService.getHeaders();
        if (headers != null) {
            Gson gson = new Gson();
            Object[][] data = gson.fromJson(headers, Object[][].class);
            setHeads(data);
        }

        String cookies = PersistentService.getCookies();
        if (cookies != null) {
            Gson gson = new Gson();

            List<MyCookie> data = gson.fromJson(cookies, TypeToken.getParameterized(List.class, MyCookie.class).getType());
            if (data != null) {
                setRequestCookies(data);
            }
        }
    }

    public String getLength() {
        if (contentLength > 1024) {
            return contentLength / 1024 + "KB";
        }
        return contentLength + "B";
    }


    public void setShortUrl(String shortUrl) {
        shortUrl = shortUrl.startsWith("/") ? shortUrl : "/" + shortUrl;
        //handle Restfull style method
        int index = shortUrl.indexOf("{");
        if (index > 0) {
            shortUrl = shortUrl.substring(0, index);
        }
        this.shortUrl = shortUrl;
    }


    public void setMethod(String method) {
        if (method != null) {
            if (method.contains(".")) {
                String[] split = method.split("\\.");
                this.method = split[1];
            } else {
                this.method = method;
            }
        }
    }

    public List<MyCookie> getRequestCookies() {
        return requestCookies;
    }

    public void setRequestCookies(List<MyCookie> requestCookies) {
        this.requestCookies = requestCookies;
    }

    public void setUrl(String url) {
        Url = url.replace("\"", "%22").replace("{", "%7b").replace("}", "%7d");
    }


    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }


    public String getShortUrl() {
        return shortUrl;
    }

    public Header[] getRequestHeads() {
        return requestHeads;
    }

    public void setRequestHeads(Header[] requestHeads) {
        this.requestHeads = requestHeads;
    }

    public Header[] getResponseHeads() {
        return responseHeads;
    }

    public void setResponseHeads(Header[] responseHeads) {
        this.responseHeads = responseHeads;
    }

    public List<Cookie> getResponseCookies() {
        return responseCookies;
    }

    public void setResponseCookies(List<Cookie> responseCookies) {
        this.responseCookies = responseCookies;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getMethod() {
        return method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return Url;
    }

    public Object[][] getHeads() {
        return heads;
    }

    public void setHeads(Object[][] heads) {
        this.heads = heads;
    }

    public Object[][] getParams() {
        return params;
    }

    public void setParams(Object[][] params) {
        this.params = params;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseBody() {
        return responseBody == null ? "" : responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Object[][] getFile() {
        return file;
    }

    public void setFile(Object[][] file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "HttpState{" +
                "description='" + description + '\'' +
                ", bodyType='" + bodyType + '\'' +
                ", Url='" + Url + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", method='" + method + '\'' +
                ", file=" + Arrays.toString(file) +
                ", heads=" + Arrays.toString(heads) +
                ", params=" + Arrays.toString(params) +
                ", requestCookies=" + requestCookies +
                ", responseCode=" + responseCode +
                ", responseTime=" + responseTime +
                ", contentLength=" + contentLength +
                ", responseBody='" + responseBody + '\'' +
                ", requestHeads=" + Arrays.toString(requestHeads) +
                ", responseHeads=" + Arrays.toString(responseHeads) +
                ", responseCookies=" + responseCookies +
                '}';
    }
}
