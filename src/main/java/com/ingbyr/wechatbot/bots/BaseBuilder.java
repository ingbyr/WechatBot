package com.ingbyr.wechatbot.bots;

import okhttp3.Request;

/**
 * Created on 17-2-22.
 *
 * @author ing
 * @version 1
 */
public abstract class BaseBuilder {
    protected final String USER_AGENT = "User-Agent";
    protected final String USER_AGENT_CONTENT = "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5";

    protected String args;
    protected Request request;
    protected String baseUrl;
    protected String url;

    public abstract BaseBuilder setArgs(String args);

    public abstract BaseBuilder initRequest();

    public abstract BaseBot build();

}
