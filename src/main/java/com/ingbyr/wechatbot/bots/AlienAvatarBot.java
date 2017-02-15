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

    @Override
    public void initUrl(String arg) {
        baseUrl = "https://robohash.org/";
        url = baseUrl + arg;
    }

    @Override
    public void initRequset() {
        request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .build();
    }

    @Override
    public String doRequest() {
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
}
