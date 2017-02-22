package com.ingbyr.wechatbot.bots;

import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created on 17-2-12.
 *
 * @author ing
 * @version 1
 */
public class AlienAvatarBot extends BaseBot {

    public AlienAvatarBot(Request request) {
        this.request = request;
    }

    @Override
    public String requestData() {
        try (ResponseBody responseBody = client.newCall(request).execute().body()) {
            byte[] image = responseBody.bytes();
            String imageUrl = System.getProperty("user.home") + "/WechatBotRun/alien.png";
            try (FileOutputStream fos = new FileOutputStream(imageUrl)) {
                fos.write(image);
            }
            return imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Builder extends BaseBuilder {

        public Builder() {
            baseUrl = "https://robohash.org/";
        }

        @Override
        public BaseBuilder setArgs(String args) {
            this.args = args;
            this.url = this.baseUrl + this.args;
            return this;
        }

        @Override
        public BaseBuilder initRequest() {
            request = new Request.Builder()
                    .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                    .url(url)
                    .build();
            return this;
        }

        @Override
        public BaseBot build() {
            return new AlienAvatarBot(request);
        }
    }
}
