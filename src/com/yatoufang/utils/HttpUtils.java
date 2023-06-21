package com.yatoufang.utils;

import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {
    private static PoolingHttpClientConnectionManager cm;
    private static CloseableHttpClient httpclient;

    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(9000000).setConnectTimeout(300000).build();// 设置请求和传输超时时间

    static {

        try {
            HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

                @Override
                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    if (executionCount >= 5) {
                        // Do not retry if over max retry count
                        return false;
                    }
                    if (exception instanceof InterruptedIOException) {
                        // Timeout
                        return false;
                    }
                    if (exception instanceof UnknownHostException) {
                        // Unknown host
                        return false;
                    }
                    if (exception instanceof SSLException) {
                        // SSL handshake exception
                        return false;
                    }
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                    if (idempotent) {
                        // Retry if the request is considered idempotent
                        return true;
                    }
                    return false;
                }

            };

            // SSL context for secure connections can be created either based on
            // system or application specific properties.
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            // Implementation of a trust manager for X509 certificates
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslcontext.init(null, new TrustManager[]{tm}, null);
            // Create a registry of custom connection socket factories for
            // supported
            // protocol schemes.
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslcontext)).build();

            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            httpclient = HttpClients.custom().setConnectionManager(cm).setRetryHandler(myRetryHandler).build();
            cm.setMaxTotal(500);
            cm.setDefaultMaxPerRoute(200);
            Integer connectionTimeout = 5000;
            Integer soTimeout = 15000;
            requestConfig = RequestConfig.custom().setSocketTimeout(soTimeout).setConnectTimeout(connectionTimeout).build();// 设置请求和传输超时时间

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String sendGet(String url, Map<String, Object> params, boolean encodeParams) {
        return sendGet(url, map2Prefix(params, encodeParams));
    }

    public static String sendGet(String url, Map<String, Object> params) {
        return sendGet(url, map2Prefix(params, false));
    }

    public static String sendGet(String url, String prefix) {
        return sendGet(getURL(url, prefix));
    }

    private static String getURL(String url, String prefix) {
        if (prefix != null && !prefix.isEmpty()) {
            if (url.indexOf("?") < 1) {
                url += "?";
            }
        }
        return url.concat(prefix);
    }

    public static String sendGet(String url, Map<String, Object> params, Map<String, String> header) {
        url = getURL(url, map2Prefix(params, true));
        return sendGetWithHeader(url, header);
    }

    public static String sendGet(String urlPath) {
        return sendGetWithHeader(urlPath, Collections.emptyMap());
    }

    public static String sendGetWithHeader(String urlPath, Map<String, String> header) {
        try {
            HttpGet httpget = new HttpGet(urlPath);
            httpget.setConfig(requestConfig);
            for (Entry<String, String> entry : header.entrySet()) {
                httpget.setHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = null;
            try {
                entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } finally {
                int statusCode = response.getStatusLine().getStatusCode();
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
                response.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String sendPost(String url, String jsonObject) {
        return sendPost(url, jsonObject, "application/json", Collections.emptyMap());
    }


    public static String sendPostJson(String url, String params, Map<String, String> headerMap) {
        return sendPost(url, params, "application/json", headerMap);
    }


    public static String sendPost(String url, Map<String, Object> paramsMaps) {
        return sendPost(url, paramsMaps, null, Collections.emptyMap());
    }

    public static String sendPost(String url, Map<String, Object> paramsMaps, String cookie) {
        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Cookie", cookie);
        return sendPost(url, paramsMaps, null, headerMap);
    }

    public static String sendPost(String url, String params, String contentType, Map<String, String> header) {
        try {
            ByteArrayEntity requestEntity = new ByteArrayEntity(params.getBytes("UTF-8"));
            if (contentType != null) {
                requestEntity.setContentType(contentType);
            }
            return sendPost(url, requestEntity, header);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String sendPost(String url, Map<String, Object> paramsMaps, String contentType, Map<String, String> header) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Entry<String, Object> entry : paramsMaps.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        try {
            UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nvps, "utf-8");
            if (contentType != null) {
                requestEntity.setContentType(contentType);
            }
            return sendPost(url, requestEntity, header);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String sendPost(String url, HttpEntity requestEntity, Map<String, String> header) {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            for (Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            httpPost.setEntity(requestEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = null;
            try {
                entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } finally {
                int statusCode = response.getStatusLine().getStatusCode();
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String map2Prefix(Map<String, Object> data, boolean encodeParams) {
        StringBuilder sb = new StringBuilder();
        try {

            for (Entry<String, Object> entry : data.entrySet()) {
                if (encodeParams) {
                    sb.append("&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "utf-8"));
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }

            if (sb.length() > 1) {
                return sb.substring(1).toString();
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        String result = sendGet("https://github.com/");
        System.out.println(result);
    }

}
