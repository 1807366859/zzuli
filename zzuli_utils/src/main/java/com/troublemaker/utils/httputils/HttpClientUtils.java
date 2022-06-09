package com.troublemaker.utils.httputils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @author Troublemaker
 * @date 2022- 04 28 20:21
 */
@Slf4j
@Configuration
public class HttpClientUtils {

    private static final int SUCCESS_CODE = 200;

    @SneakyThrows
    @Bean
    public HttpClientBuilder getHttpClientBuilder() {

        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String str) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String str) {
            }
        };
        // 创建安全协议
        SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
        sslContext.init(null, new TrustManager[]{trustManager}, null);
        // https 协议工厂
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        // 配置请求参数
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .setExpectContinueEnabled(Boolean.TRUE)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Collections.singletonList(AuthSchemes.BASIC))
                // 允许循环重定向
                .setCircularRedirectsAllowed(true)
                // 设置客户端等待服务端返回数据的超时时间
                .setSocketTimeout(3 * 1000)
                // 设置客户端发起TCP连接请求的超时时间
                .setConnectTimeout(3 * 1000)
                // 设置客户端从连接池获取链接的超时时间
                .setConnectionRequestTimeout(3 * 1000)
                .build();

        // 配置请求头
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setDefaultHeaders(headers)
                .setRedirectStrategy(new LaxRedirectStrategy());
    }

    @SneakyThrows
    public static String doGetForEntity(CloseableHttpClient client, String url) {
        CloseableHttpResponse response = null;
        HttpEntity entity;
        String entityStr = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {
                entity = response.getEntity();
                entityStr = EntityUtils.toString(entity, "utf-8");
            } else {
                log.info("响应失败：" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                // 若响应不是200，手动关闭响应
                EntityUtils.consume(response.getEntity());
            }
        }
        return entityStr;
    }

    public static String doGetForHeaders(CloseableHttpClient client, String url) {
        CloseableHttpResponse response;
        String headerStr = null;
        Header[] headers;
        try {
            HttpGet httpGet = new HttpGet(url);
            response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {
                headers = response.getHeaders("Set-Cookie");
                // %3D换成=
                headerStr = Arrays.toString(headers).replace("%3D", "=");
            } else {
                log.info("响应失败：" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headerStr;
    }

    @SneakyThrows
    public static String doApplicationPost(CloseableHttpClient client, String url, Map<String, String> map) {
        CloseableHttpResponse response = null;
        HttpEntity entity;
        String entityStr = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> parameters = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(parameters);
            httpPost.setEntity(encodedFormEntity);
            response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {
                entity = response.getEntity();
                entityStr = EntityUtils.toString(entity, "utf-8");
            } else {
                log.info("响应失败：" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                // 若响应不是200，手动关闭响应
                EntityUtils.consume(response.getEntity());
            }
        }
        return entityStr;
    }

    @SneakyThrows
    public static String doJsonPostWithHeader(CloseableHttpClient client, String url, String params, Header header) {
        CloseableHttpResponse response = null;
        HttpEntity entity;
        String entityStr = null;
        try {
            HttpPost  httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader(header);
            httpPost.setEntity(new StringEntity(params, StandardCharsets.UTF_8));
            response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {
                entity = response.getEntity();
                entityStr = EntityUtils.toString(entity, "utf-8");
            } else {
                log.info("响应失败：" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                // 若响应不是200，手动关闭响应
                EntityUtils.consume(response.getEntity());
            }
        }
        return entityStr;
    }

    public static Header getHeader(String name, String value) {
        return new Header() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
    }

    public static long timeDifference(String str) {
        URL url;
        URLConnection urlConnection;
        long localDate = 0;
        long severDate = 0;
        try {
            url = new URL(str);
            urlConnection = url.openConnection();
            localDate = System.currentTimeMillis();
            urlConnection.connect();
            severDate = urlConnection.getDate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long difference = localDate - severDate;
        return difference > 0 ? difference : 0;
    }
}
