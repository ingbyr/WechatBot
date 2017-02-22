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
    public final Logger log;
    protected Request request;
    protected final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build();

    public BaseBot() {
        log = LoggerFactory.getLogger(this.getClass());
    }


    public abstract String requestData();

}
