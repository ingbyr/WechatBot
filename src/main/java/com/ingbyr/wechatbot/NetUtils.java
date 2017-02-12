package com.ingbyr.wechatbot;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created on 17-2-7.
 *
 * @author ing
 * @version 1
 */
public class NetUtils {
    private static final Logger log = LoggerFactory.getLogger(NetUtils.class);
    private static List<Cookie> localCookie = new ArrayList<>();

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .cookieJar(new CookieJar() {
                // todo cookie本地持久化，暂时存储在内存
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                    localCookie = list;
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                    return localCookie;
                }
            })
            .build();
    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_CONTENT = "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5";

    public static String request(String url) throws IOException {
        Request request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .build();
        try (ResponseBody responseBody = client.newCall(request).execute().body()) {
            return responseBody.string();
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }

    public static byte[] requestForBytes(String url) throws IOException {
        Request request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .build();
        try (ResponseBody responseBody = client.newCall(request).execute().body()) {
            return responseBody.bytes();
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }

    public static String requestWithJson(String url, String data) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .post(body)
                .build();
        try (ResponseBody responseBody = client.newCall(request).execute().body()) {
            return responseBody.string();
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;

//        Response response = client.newCall(request).execute();
//        return response;
    }

    public static byte[] requestWithJsonForBytes(String url, String data) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .post(body)
                .build();
        try (ResponseBody responseBody = client.newCall(request).execute().body()) {
            return responseBody.bytes();
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    public static void writeToFile(String path, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(data);
        }
    }

    public static String getUrlParamsByMap(Map<String, Object> map,
                                           boolean isSort) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        List<String> keys = new ArrayList<String>(map.keySet());
        if (isSort) {
            Collections.sort(keys);
        }
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key).toString();
            sb.append(key + "=" + value);
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.lastIndexOf("&"));
        }
        return s;
    }
}
