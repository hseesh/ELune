package com.yatoufang.core;


import com.google.gson.Gson;
import com.yatoufang.entity.HttpState;
import com.yatoufang.entity.MyCookie;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * http request module,based on apache HttpClient
 *
 * @author hse
 * @Date: 2021/3/8 0008
 */
public class HttpRequest {


    public static void initRequest(HttpState state) throws Exception {
        HttpRequestBase requestBase;
        switch (state.getMethod()) {
            case "POST":
                requestBase = new HttpPost(state.getUrl());
                setBody(state, (HttpEntityEnclosingRequestBase) requestBase);
                break;
            case "PUT":
                requestBase = new HttpPut(state.getUrl());
                setBody(state, (HttpEntityEnclosingRequestBase) requestBase);
                break;
            case "DELETE":
                requestBase = new HttpDelete(state.getUrl());
                break;
            default:
                requestBase = new HttpGet(state.getUrl());
                break;
        }

        setRequestConfig(requestBase);

        if (state.getHeads() != null && state.getHeads().length > 0) {
            setHead(state, requestBase);
        }

        execute(requestBase, state);

    }


    public static void initPackRequest(HttpState state) throws Exception {
        HttpEntityEnclosingRequestBase requestBase = new HttpPost(state.getUrl());
        StringEntity entity = new StringEntity("");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        requestBase.setEntity(entity);
        execute(requestBase, state);
    }


    private static void setRequestConfig(HttpRequestBase httpRequest) {
        RequestConfig.Builder custom = RequestConfig.custom();
        custom.setConnectTimeout(5000);
        custom.setConnectionRequestTimeout(5000);
        custom.setSocketTimeout(5000);
        custom.setRedirectsEnabled(true);
        httpRequest.setConfig(custom.build());
    }

    private static void setBody(HttpState state, HttpEntityEnclosingRequestBase httpRequest) throws Exception {
        if (state.getParams() != null && state.getParams().length > 0) {
            Object[][] formData = state.getParams();
            if ("JSON".equals(state.getBodyType())) {
                String s = arrayToJson(formData);
                StringEntity entity = new StringEntity(s);
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpRequest.setEntity(entity);
            } else if("urlencoded".equals(state.getBodyType())){
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                if (formData != null) {
                    for (Object[] formDatum : formData) {
                        if (formDatum[0] != null && formDatum[1] != null && !"".equals(formDatum[0]) && !"".equals(formDatum[1])) {
                            entityBuilder.addTextBody(String.valueOf(formDatum[0]), String.valueOf(formDatum[1]));
                        }
                    }
                }
                HttpEntity entity1 = entityBuilder.build();
                httpRequest.setEntity(entity1);
            }
        }
    }

    private static String arrayToJson(Object[][] formData) {
        HashMap<Object, Object> map = new HashMap<>();
        for (Object[] formDatum : formData) {
            if (isNull(formData[0])) {
                map.put(String.valueOf(formDatum[0]), String.valueOf(formDatum[1]));
            }
        }
        Gson gson = new Gson();
        return gson.toJson(map);
    }


    private static boolean isNull(Object[] data) {
        if (data == null) return false;
        if (data.length == 1) {
            return false;
        } else {
            return !"".equals(data[0]) && !"".equals(data[1]);
        }
    }

    private static void setHead(HttpState state, HttpRequestBase httpRequest) {
        Object[][] heads = state.getHeads();
        for (Object[] head : heads) {
            if (isNull(head)) {
                httpRequest.setHeader(String.valueOf(head[0]), String.valueOf(head[1]));
            }
        }
    }


    private static void execute(HttpUriRequest httpRequest, HttpState state) throws Exception {

        CookieStore cookieStore = new BasicCookieStore();
        if (state.getRequestCookies() != null && state.getRequestCookies().size() > 0) {
            setBaseCookie(state.getRequestCookies(), cookieStore);
        }

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        try {
            long startTime = System.currentTimeMillis();
            response = httpClient.execute(httpRequest);
            long endTime = System.currentTimeMillis();

            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                state.setResponseTime(endTime - startTime);
                state.setResponseCode(response.getStatusLine().getStatusCode());
                state.setResponseBody(EntityUtils.toString(responseEntity, StandardCharsets.UTF_8));
                state.setResponseCookies(cookieStore.getCookies());
                state.setResponseHeads(response.getAllHeaders());
                state.setRequestHeads(httpRequest.getAllHeaders());
                state.setContentLength(responseEntity.getContentLength());
            }
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        }
    }

    private static void setBaseCookie(List<MyCookie> requestCookies, CookieStore cookieStore) {
        for (MyCookie myCookie : requestCookies) {
            BasicClientCookie cookie = new BasicClientCookie(myCookie.name, myCookie.value);
            if (myCookie.isSecure) {
                cookie.setSecure(true);
            }

            if (myCookie.cookieComment != null) {
                cookie.setComment(myCookie.cookieComment);
            }

            if (myCookie.cookieDomain != null) {
                cookie.setDomain(myCookie.cookieDomain);
            }

            if (myCookie.cookieExpiryDate != null) {
                cookie.setExpiryDate(myCookie.cookieExpiryDate);
            }

            if (myCookie.cookiePath != null) {
                cookie.setPath(myCookie.cookiePath);
            }

            if (myCookie.cookieVersion >= 0) {
                cookie.setVersion(myCookie.cookieVersion);
            }
            cookieStore.addCookie(cookie);
        }
    }
}
