package com.ingbyr.wechatbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .cookieJar(new CookieJar() {
                // todo cookie本地持久化，暂时存储在内存
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                    localCookie = list;
                    log.debug("cookies: " + list);
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
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }

    public static String postImage(String id, String url, File file,
                                   String mediatype, Map<String, Object> uploadMediaRequest,
                                   String passTicket) throws IOException {

        log.debug("local cookies: " + localCookie.toString());
        String webwxDataTicket = StringUtils.substringBetween(localCookie.toString(), "webwx_data_ticket=", "; expires");
        if (StringUtils.isBlank(webwxDataTicket)) {
            log.error("No webwx_data_ticket in cookies");
            return null;
        }

        String type = Files.probeContentType(file.toPath());

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", id)
                .addFormDataPart("name", file.getName())
                .addFormDataPart("type", type)
                .addFormDataPart("lastModifiedDate", LocalDate.now().getMonthValue()
                        + "/" + LocalDate.now().getDayOfMonth()
                        + "/" + LocalDate.now().getYear()
                        + ", " + LocalTime.now().withNano(0)
                        + " GMT+0800 (CST)")
                .addFormDataPart("size", String.valueOf(Files.size(file.toPath())))
                .addFormDataPart("mediatype", mediatype)
                .addFormDataPart("uploadmediarequest", mapper.writeValueAsString(uploadMediaRequest))
                .addFormDataPart("webwx_data_ticket", webwxDataTicket)
                .addFormDataPart("pass_ticket", passTicket)
                .addFormDataPart("filename", file.getName(), RequestBody.create(MediaType.parse(type), file))
                .build();

        Request request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .post(requestBody)
                .build();

        try (ResponseBody responseBody = client.newCall(request).execute().body()) {
            return responseBody.string();
        } catch (Exception e) {
            e.printStackTrace();
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
