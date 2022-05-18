package com.troublemaker.utils.httputils;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Troublemaker
 * @date 2022- 04 28 20:21
 */
public class HttpClientUtils {

    public static HttpClient getClientNoSSL() {
        try {
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
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[]{trustManager}, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            //  创建Registry
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Collections.singletonList(AuthSchemes.BASIC))
                    .setCircularRedirectsAllowed(true).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", socketFactory).build();
            // 创建ConnectionManager，添加Connection配置信息

            List<Header> headers = new ArrayList<>();
            headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
            headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
            headers.add(new BasicHeader("Accept-Language", "zh-CN"));
            headers.add(new BasicHeader("Connection", "Keep-Alive"));

            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(200);// 同时最多连接数
            connectionManager.setDefaultMaxPerRoute(100);// 设置最大路由
            // 1、MaxtTotal是整个池子的大小；
            // 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
            // MaxtTotal=400 DefaultMaxPerRoute=200
            // 而我只连接到http://www.abc.com时，到这个主机的并发最多只有200；而不是400；
            // 而我连接到http://www.bac.com 和
            // http://www.ccd.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute

            return HttpClients.custom().setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultHeaders(headers)
                    .setRedirectStrategy(new LaxRedirectStrategy())
                    .build();

        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String doGetForEntity(HttpClient client, String url) {
        String entityStr = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            entityStr = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entityStr;
    }

    public static String doGetForHeaders(HttpClient client, String url) {
        String headerStr = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);

            Header[] headers = response.getHeaders("Set-Cookie");
            // %3D换成=
            headerStr = Arrays.toString(headers).replace("%3D", "=");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return headerStr;
    }

    public static String doApplicationPost(HttpClient client, String url, Map<String, String> map) {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> parameters = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        String entityStr = null;
        try {
            UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(parameters);
            httpPost.setEntity(encodedFormEntity);
            HttpResponse response = client.execute(httpPost);
            entityStr = EntityUtils.toString(response.getEntity(), "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return entityStr;
    }

    public static String doJsonPostWithHeader(HttpClient client, String url, String params, Header header) {
        String entityStr = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader(header);
            httpPost.setEntity(new StringEntity(params, StandardCharsets.UTF_8));
            HttpResponse response = client.execute(httpPost);
            entityStr = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
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
        long localDate = 0;
        long severDate = 0;
        try {
            DateFormat Gmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            URL url = new URL(str);
            localDate = new Date().getTime();
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            severDate = Gmt.parse(httpCon.getHeaderField("Date")).getTime();

        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
        }
        long difference = localDate - severDate;
        return difference > 0 ? difference : 0;
    }

}
