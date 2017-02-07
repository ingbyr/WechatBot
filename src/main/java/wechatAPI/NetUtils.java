package wechatAPI;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created on 17-2-7.
 *
 * @author ing
 * @version 1
 */
public class NetUtils {
    private static final Logger log = LoggerFactory.getLogger(NetUtils.class);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(24, TimeUnit.DAYS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build();

    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_CONTENT = "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5";

    public static Response request(String url) throws IOException {
        Request request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response requestWithJson(String url, String data) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
