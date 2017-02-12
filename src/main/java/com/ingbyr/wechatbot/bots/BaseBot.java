package com.ingbyr.wechatbot.bots;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created on 17-2-12.
 *
 * @author ing
 * @version 1
 */
public abstract class BaseBot {
    public static final String USER_AGENT = "User-Agent";
    public static final String USER_AGENT_CONTENT = "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5";

    public final Logger log;
    public String baseUrl;
    public Request request;
    public String url;

    public final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build();

    public BaseBot() {
        log = LoggerFactory.getLogger(this.getClass());
    }

    public abstract void initUrl(String arg);

    public abstract void initRequset();

    public abstract String doRequest();

    public String start(String arg) {
        initUrl(arg);
        initRequset();
        return doRequest();
    }
}
