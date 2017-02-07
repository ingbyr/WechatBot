package wechatAPI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created on 17-2-7.
 *
 * @author ing
 * @version 1
 */
public class NetUtils {
    public static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(24, TimeUnit.DAYS)
            .readTimeout(5,TimeUnit.MINUTES)
            .writeTimeout(5,TimeUnit.MINUTES)
            .build();

    public static Response request(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
